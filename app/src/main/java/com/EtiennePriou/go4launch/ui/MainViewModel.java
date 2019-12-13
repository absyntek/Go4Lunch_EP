package com.EtiennePriou.go4launch.ui;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {


    private Location mLocation;
    private MutableLiveData<String> currentName = new MutableLiveData<>();
    private int wichFrag = 1;

    public MainViewModel() {
        mLocation = null;
    }

    // --- GETTERS ---
    int getWichFrag() {
        return wichFrag;
    }
    public Location getLocation() {
        return mLocation;
    }
    public MutableLiveData<String> getWorkmateSearch() {
        return currentName;
    }

    // --- SETTERS ---
    void setWichFrag(int wichFrag) {
        this.wichFrag = wichFrag;
    }
    void setCurrentName(String currentName) {
        this.currentName.setValue(currentName);
    }
    public void setLocation(Location location) {
        mLocation = location;
    }
}
