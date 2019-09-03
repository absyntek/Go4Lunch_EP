package com.EtiennePriou.go4launch.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.places.PlacesApi;
import com.google.android.gms.tasks.OnFailureListener;

public abstract class BaseActivity extends AppCompatActivity {

    protected PlacesApi mPlacesApi;
    protected FireBaseApi mFireBaseApi;

    protected abstract int getLayoutContentViewID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getLayoutContentViewID());
        mPlacesApi = DI.getServiceApiPlaces();
        mFireBaseApi = DI.getServiceFireBase();
        setupUi();
        withOnCreate();
    }

    protected abstract void setupUi();

    protected abstract void withOnCreate();


}
