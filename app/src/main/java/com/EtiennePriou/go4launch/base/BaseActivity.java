package com.EtiennePriou.go4launch.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;

public abstract class BaseActivity extends AppCompatActivity {

    protected PlacesApiService mPlacesApiService;

    public abstract int getLayoutContentViewID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutContentViewID());
        mPlacesApiService = DI.getServiceApiPlaces();
        setupUi();
        withOnCreate();
    }

    protected abstract void setupUi();

    protected abstract void withOnCreate();


}
