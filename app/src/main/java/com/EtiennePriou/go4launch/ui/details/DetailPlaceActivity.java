package com.EtiennePriou.go4launch.ui.details;

import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.PlaceToGo;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.services.places.PlacesApi;
import com.EtiennePriou.go4launch.services.places.helpers.DetailHelper;
import com.EtiennePriou.go4launch.ui.fragments.workmates_list.MyWorkmateRecyclerViewAdapter;
import com.EtiennePriou.go4launch.utils.NoteCalcul;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderView;

import java.util.Objects;

public class DetailPlaceActivity extends BaseActivity {

    private ImageView mimgNoOne, mimgDetailsTop;
    private SliderView mSliderView;
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
    private MyWorkmateRecyclerViewAdapter mAdapter;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_detail_place;
    }

    @Override
    protected void setupUi() {

        mimgNoOne = findViewById(R.id.imgNoOne);
        mratingBarPlace = findViewById(R.id.ratingPlaceNote);
        mimgDetailsTop = findViewById(R.id.imgDetailsTop);
        mSliderView = findViewById(R.id.imageSlider);
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
        placeRef = getIntent().getStringExtra(getResources().getString(R.string.PLACEREFERENCE));

        getPlaceDetails();
    }

    /**
     * get place detail from GooglePlace
     */
    private void getPlaceDetails () {
        setUpRecyclerView(PlaceHelper.getUsersToday(placeRef));
        DetailHelper.getDetails(placeRef, mPlacesApi.getPlacesClient()).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                mPlaceDetails = response.getPlace();
                checkNote();
                setMiddleBarButtons();
                setFabButton();
                updateUi();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    mPlaceDetails = null;
                }
            }
        });
    }

    /**
     * upDate ui components
     */
    private void updateUi() {
        if (mPlaceDetails.getPhotoMetadatas() != null){
            if (!mPlaceDetails.getPhotoMetadatas().isEmpty()){
                mSliderView.setVisibility(View.VISIBLE);
                mimgDetailsTop.setVisibility(View.GONE);
                mSliderView.setSliderAdapter(new SliderAdapter(mPlaceDetails.getPhotoMetadatas()));
            }
        }else {
            mSliderView.setVisibility(View.GONE);
            mimgDetailsTop.setVisibility(View.VISIBLE);
        }
        mtvPlaceName.setText(mPlaceDetails.getName());
        mtvPlaceAdresse.setText(mPlaceDetails.getAddress());
    }

    /**
     * get notesList from server
     * and update UI
     */
    private void checkNote(){
        PlaceHelper.getNotes(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null){
                    mratingBarPlace.setRating( NoteCalcul.calateNote(queryDocumentSnapshots));
                }
            }
        });
    }

    /**
     * set action on button press
     * CALL Button
     * RATE Button
     * WEB Button
     */
    private void setMiddleBarButtons() {

        /* --- Call Button --- */
        if (mPlaceDetails.getPhoneNumber() == null || mPlaceDetails.getPhoneNumber().isEmpty()){
            mbtnCall.setBackgroundColor(getResources().getColor(R.color.grey));
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

        /* --- Rate Button --- */
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

    /**
     * return the note given by actual user for ratting dialBox
     */
    private void getMyNote(){
        PlaceHelper.getMyNote(placeRef, mFireBaseApi.getCurrentUser().getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getDouble("note") != null){
                    int myNote = documentSnapshot.get("note",Integer.class);
                    createDialogBox(myNote);
                }else createDialogBox(0);
            }
        });
    }

    /**
     * create the Ratting box with note given if not null
     */
    private void createDialogBox(int myNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    /**
     * update note on Server
     */
    private void sendNote(int note) {
        PlaceHelper.createNote(mFireBaseApi.getCurrentUser().getUid(),placeRef,note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(DetailPlaceActivity.this, mContext.getString(R.string.the_note_sent), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Live Query for users who comming this place
     */
    private void setUpRecyclerView (Query query){
        mAdapter = new MyWorkmateRecyclerViewAdapter(generateOptionsForAdapter(query),0);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(this.mAdapter);
    }
    //Create options for RecyclerView from a Query
    private FirestoreRecyclerOptions<Workmate> generateOptionsForAdapter(Query query){
        return new FirestoreRecyclerOptions
                .Builder<Workmate>()
                .setQuery(query,Workmate.class)
                .build();
    }

    /**
     * set Fab for modify whoComming
     */
    private void setFabButton (){
        UserHelper.getUser(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        currentUser = documentSnapshot.toObject(Workmate.class);
                        if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().getPlaceRef().equals(mPlaceDetails.getId())){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                fab.getDrawable().setTint(getResources().getColor(R.color.green));
                            }
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                fab.getDrawable().setTint(getResources().getColor(R.color.grey));
                            }
                        }

                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                PlaceToGo placeToGo = new PlaceToGo(mPlaceDetails.getAddress(),mPlaceDetails.getName(),placeRef);

                                // If actual user said he wanna come here so we delete him from list
                                if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().getPlaceRef().equals(placeRef)){

                                    currentUser.setPlaceToGo(null);

                                    PlaceHelper.deleteUserWhoComming(currentUser.getUid(),mPlaceDetails.getId());
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),null);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.grey));
                                    }
                                    makeToast(getResources().getString(R.string.no_going_there));

                                }
                                // If actual user said he wanna come to an other place so we delete
                                // him from the other place list and add him to the new place
                                else if (currentUser.getPlaceToGo() != null && !currentUser.getPlaceToGo().getPlaceRef().equals(placeRef)){

                                    PlaceHelper.deleteUserWhoComming(currentUser.getUid(),currentUser.getPlaceToGo().getPlaceRef());

                                    currentUser.setPlaceToGo(placeToGo);
                                    currentUser.setDateCreated(null);

                                    PlaceHelper.createWhoComing(mPlaceDetails.getId(), currentUser);
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),placeToGo);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.green));
                                    }
                                    makeToast(getResources().getString(R.string.nice_choice));

                                }
                                //actual user did not choose so we add him to this place
                                else{
                                    currentUser.setPlaceToGo(placeToGo);
                                    currentUser.setDateCreated(null);

                                    PlaceHelper.createWhoComing(mPlaceDetails.getId(), currentUser);
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),placeToGo);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.green));
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

    /**
     * Toaster
     */
    private void makeToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
