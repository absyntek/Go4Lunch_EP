package com.EtiennePriou.go4launch.ui.details;

import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.di.ViewModelFactory;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.services.places.PlacesApi;
import com.EtiennePriou.go4launch.utils.DetailHelper;
import com.EtiennePriou.go4launch.ui.fragments.workmates_list.MyWorkmateRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.EtiennePriou.go4launch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailPlaceActivity extends BaseActivity {

    private static final String PLACEREFERENCE = "placeReference";

    private ImageView mimgNoOne, mimgDetailsTop;
    private RatingBar mratingBarPlace, ratingBar;
    private TextView mtvNoOne, mtvPlaceName, mtvPlaceAdresse;
    private Button mbtnCall, mbtnLike, mbtnWebsite;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;
    private AlertDialog alertDialog;

    private PlacesApi mPlacesApi;
    private Place mPlaceDetails;
    private String placeRef;
    private Workmate currentUser;
    private int placeNote;
    private int impToFinish = 0;

    private DetailsViewModel mDetailsViewModel;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_detail_place;
    }

    @Override
    protected void setupUi() {

        mimgNoOne = findViewById(R.id.imgNoOne);
        mratingBarPlace = findViewById(R.id.ratingPlaceNote);
        mimgDetailsTop = findViewById(R.id.imgDetailsTop);
        mtvNoOne = findViewById(R.id.tvNoOne);
        mtvPlaceName = findViewById(R.id.tvNameDetails);
        mtvPlaceAdresse =findViewById(R.id.tvAdresseDetails);
        mbtnCall = findViewById(R.id.btnCall);
        mbtnLike = findViewById(R.id.btnLike);
        mbtnWebsite = findViewById(R.id.btnWebsite);
        mRecyclerView = findViewById(R.id.recyclerviewDetails);
        fab = findViewById(R.id.fab);
    }

    @Override
    protected void withOnCreate() {
        mPlacesApi = DI.getServiceApiPlaces();
        configureViewModel();

        placeRef = getIntent().getStringExtra(PLACEREFERENCE);
        getPlaceDetails();
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = DI.provideViewModelFactory();
        mDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel.class);
    }

    private void getPlaceDetails () {

        DetailHelper.getDetails(placeRef, mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                Place place = response.getPlace();
                Log.i("test", "Place found: " + place.getName());
                mPlaceDetails = place;

                checkNote();
                setMiddleBarButtons();
                setFabButton();

                if (mDetailsViewModel.getWorkmatesThisPlace() == null){
                    mDetailsViewModel.setWorkmatesThisPlace(new ArrayList<Workmate>());
                    checkWorkmateComeHere();
                }else{
                    if (mDetailsViewModel.getWorkmatesThisPlace().isEmpty()) {
                        changeUiIfNoWorkmateHere();
                    }else{
                        setUpRecyclerView();
                    }
                }

                updateUi();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e("test", "Place not found: " + exception.getMessage());
                    mPlaceDetails = null;
                }
            }
        });
    }

    private void updateUi() {
        DetailHelper.getPhoto(mPlaceDetails, mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPhotoResponse>() {
            @Override
            public void onSuccess(FetchPhotoResponse fetchPhotoResponse) {
                Bitmap bitmap = fetchPhotoResponse.getBitmap();
                Glide.with(mimgDetailsTop).load(bitmap).into(mimgDetailsTop);
            }
        });
        mtvPlaceName.setText(mPlaceDetails.getName());
        mtvPlaceAdresse.setText(mPlaceDetails.getAddress());
    }

    private void checkNote(){
        PlaceHelper.getNotes(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null || queryDocumentSnapshots.size() > 0){
                    int divisor = queryDocumentSnapshots.size();
                    int noteToDivise = 0;
                    for (DocumentSnapshot note : queryDocumentSnapshots){
                        int noteTmp = note.get("note",Integer.class);
                        noteToDivise += noteTmp;
                    }
                    if (noteToDivise < 1){
                        placeNote = 0;
                    }else{
                        placeNote = Math.round(noteToDivise/divisor);
                    }
                    mratingBarPlace.setRating(placeNote);
                }
            }
        });
    }

    private void setMiddleBarButtons() {

        /* --- Phone Button --- */
        if (mPlaceDetails.getPhoneNumber() == null || mPlaceDetails.getPhoneNumber().isEmpty()){
            mbtnCall.setBackgroundColor(getResources().getColor(R.color.grey)); //TODO Change color icon
        }else{
            mbtnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+mPlaceDetails.getPhoneNumber()));

                    if (ActivityCompat.checkSelfPermission(DetailPlaceActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    startActivity(callIntent);
                }
            });
        }

        /* --- Note Button --- */
        mbtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyNote();
            }
        });

        /* --- Web Button --- */
        if (mPlaceDetails.getWebsiteUri() == null){
                mbtnWebsite.setBackgroundColor(getResources().getColor(R.color.grey));
        }else {
            mbtnWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(mPlaceDetails.getWebsiteUri());
                    startActivity(webIntent);
                }
            });
        }
    }

    private void getMyNote(){
        PlaceHelper.getMyNote(placeRef, mFireBaseApi.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getDouble("note") != null){
                    int myNote = documentSnapshot.get("note", Integer.class);
                    createDialogBox(myNote);
                }else createDialogBox(0);
            }
        });
    }

    private void createDialogBox(int myNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutInflater inflater = this.getLayoutInflater();
            builder.setView(inflater.inflate(R.layout.dialog_rating, null));
            alertDialog = builder.create();
            alertDialog.show();

            ratingBar = alertDialog.findViewById(R.id.ratingBoxNote);
            ratingBar.setMax(5);
            ratingBar.setRating(myNote);

            Button valid = alertDialog.findViewById(R.id.ratingBoxValid);
            valid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ratingBar.getRating() > 0){
                        int note = Math.round(ratingBar.getRating());
                        sendNote(note);
                        checkNote();
                        alertDialog.dismiss();
                    }
                }
            });

            Button cancel = alertDialog.findViewById(R.id.ratingBoxCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
        }
    }

    private void sendNote(int note) {
        PlaceHelper.createFavorite(mFireBaseApi.getCurrentUser().getUid(),placeRef,note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailPlaceActivity.this, "note send", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkWorkmateComeHere() {
        PlaceHelper.getWhoComing(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.getDocuments().isEmpty()){
                    final int forFinish = queryDocumentSnapshots.size();
                    for (DocumentSnapshot userRef : queryDocumentSnapshots.getDocuments()){
                        UserHelper.getUser(userRef.get("uid").toString())
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (!Objects.equals(documentSnapshot.get("uid"), currentUser.getUid())){
                                    List<Workmate> tmp = mDetailsViewModel.getWorkmatesThisPlace();
                                    tmp.add(documentSnapshot.toObject(Workmate.class));
                                    mDetailsViewModel.setWorkmatesThisPlace(tmp);
                                }
                                impToFinish++; //TODO change this RX Java
                                if (impToFinish == forFinish){
                                    if (mDetailsViewModel.getWorkmatesThisPlace().isEmpty()) {
                                        mtvNoOne.setText(getString(R.string.onlyOne));
                                        changeUiIfNoWorkmateHere();
                                    }else{
                                        setUpRecyclerView();
                                    }
                                }
                            }
                        });
                    }
                }else  changeUiIfNoWorkmateHere();
            }
        });
    }

    private void setUpRecyclerView (){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new MyWorkmateRecyclerViewAdapter(mDetailsViewModel.getWorkmatesThisPlace(),0));
    }

    private void setFabButton (){
        UserHelper.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        currentUser = documentSnapshot.toObject(Workmate.class);
                        if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().get("placeRef").toString().equals(mPlaceDetails.getId())){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                fab.getDrawable().setTint(getResources().getColor(R.color.green)); //TOdo Change color
                            }
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                fab.getDrawable().setTint(getResources().getColor(R.color.grey)); //TOdo Change color
                            }
                        }

                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Map<String, Object> placeToGo = new HashMap<>();
                                if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().get("placeRef").toString().equals(placeRef)){

                                    PlaceHelper.deleteUserWhoComming(currentUser.getUid(),mPlaceDetails.getId());
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),null);

                                    currentUser.setPlaceToGo(null);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.grey));//TOdo Change color
                                    }
                                    makeToast(getResources().getString(R.string.no_going_there));

                                }else if (currentUser.getPlaceToGo() != null && !currentUser.getPlaceToGo().get("placeRef").toString().equals(placeRef)){
                                    placeToGo.put("placeRef",placeRef);
                                    placeToGo.put("placeName",mPlaceDetails.getName());
                                    placeToGo.put("adresse", mPlaceDetails.getAddress());

                                    PlaceHelper.deleteUserWhoComming(currentUser.getUid(),currentUser.getPlaceToGo().get("placeRef").toString());
                                    PlaceHelper.createWhoComing(mPlaceDetails.getId(),currentUser.getUid(),currentUser.getUsername());
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),placeToGo);

                                    currentUser.setPlaceToGo(placeToGo);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.green)); //TOdo Change color
                                    }
                                    makeToast(getResources().getString(R.string.nice_choice));

                                }else{
                                    placeToGo.put("placeRef",placeRef);
                                    placeToGo.put("placeName",mPlaceDetails.getName());
                                    placeToGo.put("adresse", mPlaceDetails.getAddress());

                                    PlaceHelper.createWhoComing(mPlaceDetails.getId(), currentUser.getUid(), currentUser.getUsername());
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),placeToGo);

                                    currentUser.setPlaceToGo(placeToGo);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.green)); //TOdo Change color
                                    }
                                    makeToast(getResources().getString(R.string.nice_choice));
                                }

                            }
                        });
                    }
                });
    }

    private void changeUiIfNoWorkmateHere(){
        if (mimgNoOne.getVisibility() == View.VISIBLE){
            mimgNoOne.setVisibility(View.GONE);
            mtvNoOne.setVisibility(View.GONE);
        }else {
            mimgNoOne.setVisibility(View.VISIBLE);
            mtvNoOne.setVisibility(View.VISIBLE);
        }
    }

    private void makeToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
