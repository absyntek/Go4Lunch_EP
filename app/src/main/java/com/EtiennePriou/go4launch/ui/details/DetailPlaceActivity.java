package com.EtiennePriou.go4launch.ui.details;

import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.di.ViewModelFactory;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.EtiennePriou.go4launch.ui.fragments.workmates_list.MyWorkmateRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

    private final String TAG = "Error_DetailsActivity";

    private static final String PLACEREFERENCE = "placeReference";

    private ImageView mimgNoOne, mimgDetailsTop, mimgStar;
    private TextView mtvNoOne, mtvPlaceName, mtvPlaceAdresse;
    private Button mbtnCall, mbtnLike, mbtnWebsite;
    private RecyclerView mRecyclerView;
    private FloatingActionButton fab;

    private PlaceModel mPlaceModel;
    private String placeRef;
    private Workmate currentUser;
    private int impToFinish = 0;

    private DetailsViewModel mDetailsViewModel;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_detail_place;
    }

    @Override
    protected void setupUi() {

        mimgNoOne = findViewById(R.id.imgNoOne);
        mimgStar = findViewById(R.id.imgStarDetails);
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
        configureViewModel();

        placeRef = getIntent().getStringExtra(PLACEREFERENCE);
        mPlaceModel = mPlacesApi.getPlaceByReference(placeRef);

        if (mDetailsViewModel.getFav() == null) checkFav();
        else setBarButton();

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

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = DI.provideViewModelFactory();
        mDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(DetailsViewModel.class);
    }

    private void checkFav (){
        UserHelper.getSpecFavExist(mFireBaseApi.getCurrentUser().getUid(),placeRef)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mDetailsViewModel.setFav(documentSnapshot.exists());
                        setBarButton();
                    }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+ e.getMessage());
                    }
        });
    }

    private void setBarButton() {

        if (mPlaceModel.getPhonenumber() == null || mPlaceModel.getPhonenumber().isEmpty()){
            mbtnCall.setBackgroundColor(getResources().getColor(R.color.grey)); //TODO Change color icon
        }else{
            mbtnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+mPlaceModel.getPhonenumber()));

                    if (ActivityCompat.checkSelfPermission(DetailPlaceActivity.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        return;
                    }
                    startActivity(callIntent);
                }
            });
        }

        if (mDetailsViewModel.getFav()){
            mbtnLike.setBackgroundColor(getResources().getColor(R.color.green)); //TODO Change color icon
            mimgStar.setVisibility(View.VISIBLE);
        }else {
            mbtnLike.setBackgroundColor(getResources().getColor(R.color.grey));
            mimgStar.setVisibility(View.INVISIBLE);
        }
        mbtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDetailsViewModel.getFav()){
                    UserHelper.deleteFav(mFireBaseApi.getCurrentUser().getUid(),placeRef);
                    PlaceHelper.deleteUserInFav(mFireBaseApi.getCurrentUser().getUid(),placeRef);
                    mimgStar.setVisibility(View.INVISIBLE);
                    mDetailsViewModel.setFav(false);
                }else{
                    UserHelper.createUserFav(mFireBaseApi.getCurrentUser().getUid(),placeRef, mPlaceModel.getName());
                    PlaceHelper.createFavorite(mFireBaseApi.getCurrentUser().getUid(),placeRef);
                    mimgStar.setVisibility(View.VISIBLE);
                    mbtnLike.setBackgroundColor(getResources().getColor(R.color.grey));
                    mDetailsViewModel.setFav(true);
                }
            }
        });

        if (mPlaceModel.getWebSite() == null || mPlaceModel.getWebSite().isEmpty()){
                mbtnWebsite.setBackgroundColor(getResources().getColor(R.color.grey));
        }else {
            mbtnWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(mPlaceModel.getWebSite()));
                    startActivity(webIntent);
                }
            });
        }
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

    private void updateUi() {
        if (mPlaceModel.getImgReference() != null) {
            Glide.with(mimgDetailsTop).load(mPlaceModel.getPhotoUri()).into(mimgDetailsTop);
        }
        mtvPlaceName.setText(mPlaceModel.getName());
        mtvPlaceAdresse.setText(mPlaceModel.getAdresse());
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
                        if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().get("placeRef").toString().equals(mPlaceModel.getReference())){
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

                                    PlaceHelper.deleteUserWhoComming(currentUser.getUid(),mPlaceModel.getReference());
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),null);

                                    currentUser.setPlaceToGo(null);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.grey));//TOdo Change color
                                    }
                                    makeToast(getResources().getString(R.string.no_going_there));

                                }else if (currentUser.getPlaceToGo() != null && !currentUser.getPlaceToGo().get("placeRef").toString().equals(placeRef)){
                                    placeToGo.put("placeRef",placeRef);
                                    placeToGo.put("placeName",mPlaceModel.getName());
                                    placeToGo.put("adresse", mPlaceModel.getAdresse());

                                    PlaceHelper.deleteUserWhoComming(currentUser.getUid(),currentUser.getPlaceToGo().get("placeRef").toString());
                                    PlaceHelper.createWhoComing(mPlaceModel.getReference(),currentUser.getUid());
                                    UserHelper.updatePlaceToGo(currentUser.getUid(),placeToGo);

                                    currentUser.setPlaceToGo(placeToGo);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        fab.getDrawable().setTint(getResources().getColor(R.color.green)); //TOdo Change color
                                    }
                                    makeToast(getResources().getString(R.string.nice_choice));

                                }else{
                                    placeToGo.put("placeRef",placeRef);
                                    placeToGo.put("placeName",mPlaceModel.getName());
                                    placeToGo.put("adresse", mPlaceModel.getAdresse());

                                    PlaceHelper.createWhoComing(mPlaceModel.getReference(),currentUser.getUid());
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
