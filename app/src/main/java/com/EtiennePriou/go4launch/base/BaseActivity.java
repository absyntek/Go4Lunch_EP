package com.EtiennePriou.go4launch.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.places.PlacesApi;

public abstract class BaseActivity extends AppCompatActivity {

    protected FireBaseApi mFireBaseApi;
    protected Context mContext;

    protected abstract int getLayoutContentViewID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutContentViewID());
        mContext = getApplicationContext();
        mFireBaseApi = DI.getServiceFireBase();
        setupUi();
        withOnCreate();
    }

    protected abstract void setupUi();

    protected abstract void withOnCreate();


}
