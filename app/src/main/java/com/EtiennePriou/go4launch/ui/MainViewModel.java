package com.EtiennePriou.go4launch.ui;

import android.location.Location;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class MainViewModel extends ViewModel {

    private Fragment[] mFragments;
    private GoogleMap mMap;
    private LatLng mLatLng;
    private Location mLocation;

    public MainViewModel() {
        mFragments = new Fragment[] {null,null,null};
        mMap = null;
        mLatLng = null;
        mLocation = null;
    }

    public Fragment[] getFragments() {
        return mFragments;
    }

    public Fragment getFragment(int i) {
        return mFragments[i];
    }

    public void setFragments(Fragment[] fragments) {
        mFragments = fragments;
    }

    public void setFragment(int i, Fragment fragments) {
        mFragments[i] = fragments;
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng latLng) {
        mLatLng = latLng;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public String getDistanceBetween(double lat, double longit) {

        Location endPoint = new Location("locationA");
        endPoint.setLatitude(lat);
        endPoint.setLongitude(longit);
        double distanceTo = mLocation.distanceTo(endPoint);
        String distanceToStr;
        if (distanceTo >= 1000){
            distanceTo = distanceTo/1000;
            DecimalFormat  df = new DecimalFormat("#.#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            distanceToStr = df.format(distanceTo) + "Km";
        }else {
            DecimalFormat  df = new DecimalFormat("#");
            df.setRoundingMode(RoundingMode.HALF_UP);
            distanceToStr = df.format(distanceTo) + "m"; }
        return distanceToStr;
    }
}
