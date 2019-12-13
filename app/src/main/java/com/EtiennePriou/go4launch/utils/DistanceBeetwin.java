package com.EtiennePriou.go4launch.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DistanceBeetwin {
    public static String calculDistance (LatLng endPoint, Location myLocation) {
        Location endLocation = new Location("locationA");
        endLocation.setLatitude(endPoint.latitude);
        endLocation.setLongitude(endPoint.longitude);
        if (myLocation != null){
            double distanceTo = myLocation.distanceTo(endLocation);
            String distanceToStr;
            if (distanceTo >= 1000){
                distanceTo = distanceTo/1000;
                DecimalFormat df = new DecimalFormat("#.#");
                df.setRoundingMode(RoundingMode.HALF_UP);
                distanceToStr = df.format(distanceTo) + "Km";
            }else {
                DecimalFormat  df = new DecimalFormat("#");
                df.setRoundingMode(RoundingMode.HALF_UP);
                distanceToStr = df.format(distanceTo) + "m"; }
            return distanceToStr;
        }else return "---";
    }
}