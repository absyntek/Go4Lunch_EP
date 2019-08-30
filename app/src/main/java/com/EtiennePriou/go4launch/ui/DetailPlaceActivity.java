package com.EtiennePriou.go4launch.ui;

import com.EtiennePriou.go4launch.base.BaseActivity;
import com.EtiennePriou.go4launch.models.Places;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;

import com.EtiennePriou.go4launch.R;

public class DetailPlaceActivity extends BaseActivity {

    private String reference;
    private Places mPlaces;
    private ImageView imgTop;
    private static final String PLACEREFERENCE = "placeReference";

    @Override
    public int getLayoutContentViewID() {
        return R.layout.activity_detail_place;
    }

    @Override
    protected void setupUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgTop = findViewById(R.id.imgTopDetails);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void withOnCreate() {
        reference = getIntent().getStringExtra(PLACEREFERENCE);
        mPlaces = mPlacesApiService.getPlaceByReference(reference);
        updateUi();
    }

    private void updateUi(){
        Glide.with(imgTop.getContext())
                .load(mPlaces.getPhotoUri())
                .into(imgTop);
    }
}
