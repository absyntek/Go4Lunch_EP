package com.EtiennePriou.go4launch.base;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.EtiennePriou.go4launch.R;
import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;
import com.google.android.gms.tasks.OnFailureListener;

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

    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_LONG).show();
            }
        };
    }

    protected abstract void setupUi();

    protected abstract void withOnCreate();


}
