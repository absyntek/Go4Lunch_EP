package com.EtiennePriou.go4launch.ui;

import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.PlaceHelper;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.EtiennePriou.go4launch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailPlaceActivity extends BaseActivity {

    private static final String PLACEREFERENCE = "placeReference";//TODO put in string file

    private ImageView mimgNoOne;
    private TextView mtvNoOne;
    private Button mbtnCall, mbtnLike, mbtnWebsite;
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout imgDetails;
    private FloatingActionButton fab;

    private List<Workmate> mWorkmatesThisPlace;

    private PlaceModel mPlaceModel;
    private String placeRef;
    private Workmate currentUser;
    private int impToFinish = 0;

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_detail_place;
    }

    @Override
    protected void setupUi() {

        mimgNoOne = findViewById(R.id.imgNoOne);
        imgDetails = findViewById(R.id.toolbar_layout_details);
        mtvNoOne = findViewById(R.id.tvNoOne);
        mbtnCall = findViewById(R.id.btnCall);
        mbtnLike = findViewById(R.id.btnLike);
        mbtnWebsite = findViewById(R.id.btnWebsite);
        mRecyclerView = findViewById(R.id.recyclerviewDetails);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
    }

    @Override
    protected void withOnCreate() {
        placeRef = getIntent().getStringExtra(PLACEREFERENCE);
        mPlaceModel = mPlacesApi.getPlaceByReference(placeRef);
        mWorkmatesThisPlace = new ArrayList<>();

        setFabButton();
        checkWorkmateComeHere();
        updateUi();
    }

    private void checkWorkmateComeHere() {
        PlaceHelper.getWhoComing(placeRef).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.getDocuments().isEmpty()){
                    final int forFinish = queryDocumentSnapshots.size();
                    for (DocumentSnapshot userRef : queryDocumentSnapshots.getDocuments()){
                        UserHelper.getUser(userRef.get("userRef").toString())
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (!documentSnapshot.get("uid").equals(currentUser.getUid())){
                                    mWorkmatesThisPlace.add(documentSnapshot.toObject(Workmate.class));
                                }
                                impToFinish++; //TODO change this
                                if (impToFinish == forFinish){ setUpRecyclerView(); }
                            }
                        });
                    }
                }else  changeUiIfNoWorkmateHere();
            }
        });
    }

    private void updateUi() {
        if (mPlaceModel.getImgReference() != null) {
            Glide.with(this).load(mPlaceModel.getPhotoUri()).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    imgDetails.setBackground(resource);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
        toolbar.setTitle(mPlaceModel.getName());
        toolbar.setSubtitle(mPlaceModel.getAdresse());

    }

    private void setUpRecyclerView (){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(new DetailPlaceActivityRecyclerViewAdapter(mWorkmatesThisPlace));
    }

    private void setFabButton (){
        UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        currentUser = documentSnapshot.toObject(Workmate.class);
                        if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().equals(mPlaceModel.getReference())){
                            //TODO change color
                        }
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UserHelper.getUser(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                currentUser = documentSnapshot.toObject(Workmate.class);
                                                if (currentUser.getPlaceToGo() != null && currentUser.getPlaceToGo().equals(placeRef)){
                                                    PlaceHelper.deleteUserWhoComming(mPlaceModel.getReference(),currentUser.getUid());
                                                    UserHelper.updatePlaceToGo(currentUser.getUid(),null);
                                                    //TODO Envoyer un petit message de confirmation
                                                }else{
                                                    PlaceHelper.createWhoComing(mPlaceModel.getReference(),currentUser.getUid());
                                                    UserHelper.updatePlaceToGo(currentUser.getUid(),mPlaceModel.getReference());
                                                    //TODO Envoyer un petit message de confirmation
                                                }
                                            }
                                        });
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
}
