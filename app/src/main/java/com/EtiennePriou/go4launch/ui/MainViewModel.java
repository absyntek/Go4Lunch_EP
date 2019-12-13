package com.EtiennePriou.go4launch.ui;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainViewModel extends ViewModel {


    private Location mLocation;
    private MutableLiveData<String> currentName = new MutableLiveData<>();
    private int wichFrag = 1;

    public MainViewModel() {
        mLocation = null;
    }

    public int getWichFrag() {
        return wichFrag;
    }

    public void setWichFrag(int wichFrag) {
        this.wichFrag = wichFrag;
    }

    public MutableLiveData<String> getWorkmateSearch() {
        return currentName;
    }

    void setCurrentName(String currentName) {
        this.currentName.setValue(currentName);
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Location getLocation() {
        return mLocation;
    }
}
