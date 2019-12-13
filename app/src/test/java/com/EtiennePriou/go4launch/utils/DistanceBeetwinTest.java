package com.EtiennePriou.go4launch.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.*;

public class DistanceBeetwinTest {

    @Test
    public void calculDistance() {
        LatLng latLng = new LatLng(123,145);
        Location location = new Location("locationA");
        location.setLongitude(123);
        location.setLatitude(2345);
        String s = DistanceBeetwin.calculDistance(latLng,location);
    }
}