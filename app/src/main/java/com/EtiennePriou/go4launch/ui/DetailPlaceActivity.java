package com.EtiennePriou.go4launch.ui;

import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.UserHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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

import java.util.ArrayList;
import java.util.List;

public class DetailPlaceActivity extends BaseActivity {

    private static final String PLACEREFERENCE = "placeReference";

    private ImageView mimgDetailTop, mimgNoOne;
    private TextView mtvNoOne;
    private Button mbtnCall, mbtnLike, mbtnWebsite;
    private RecyclerView mRecyclerView;
    private Toolbar toolbar;
    private CollapsingToolbarLayout imgDetails;

    private List<Workmate> mWorkmatesThisPlace;

    private PlaceModel mPlaceModel;
    private String reference;
    private FirebaseUser currentUser;

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
        FloatingActionButton fab = findViewById(R.id.fab);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                UserHelper.updatePlaceToGo(currentUser.getUid(),mPlaceModel.getReference());
            }
        });
    }

    @Override
    protected void withOnCreate() {
        reference = getIntent().getStringExtra(PLACEREFERENCE);
        mPlaceModel = mPlacesApi.getPlaceByReference(reference);
        mWorkmatesThisPlace = new ArrayList<>();
        checkWorkmateComeHere();
        updateUi();
        if (mWorkmatesThisPlace.isEmpty()){ changeUiIfNoWorkmateHere(); }
        else setUpRecyclerView();
    }

    private void checkWorkmateComeHere() {
        for (Workmate workmate : mFireBaseApi.getWorkmatesList()){
            if (workmate.getPlaceToGo() != null && workmate.getPlaceToGo().equals(mPlaceModel.getReference())){
                mWorkmatesThisPlace.add(workmate);
            }
        }
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
