package com.EtiennePriou.go4launch.services;

import android.util.Log;

import com.EtiennePriou.go4launch.utils.GetNearbyPlacesData;
import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.List;

public class DummyPlacesApiService implements PlacesApiService {

    private Integer proximity_radius = 10000;
    private List<HashMap<String, String>> mNearbyPlaceList;
    private String urlNearbyPlace;
    private GoogleMap mMap;
    private Object dataTransfer[];
    GetNearbyPlacesData getNearbyPlacesData;


    @Override
    public void setProximity_radius(Integer proximity_radius) {
        this.proximity_radius = proximity_radius;
    }

    @Override
    public Integer getProximity_radius() {
        return this.proximity_radius;
    }

    @Override
    public void setListPlaces(double latitude, double longitude, GoogleMap map) {
        setUrlNearbyPlace(latitude,longitude);
        setMap(map);
        dataTransfer = new Object[2];
        dataTransfer[0] = this.mMap;
        dataTransfer[1] = this.urlNearbyPlace;
        getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(dataTransfer);
    }

    @Override
    public List<HashMap<String, String>> getNearbyPlacesList() {
        return this.mNearbyPlaceList;
    }

    @Override
    public void setNearbyPlaceList(List<HashMap<String, String>> nearbyPlaceList) {
        this.mNearbyPlaceList=nearbyPlaceList;
    }

    private void setMap(GoogleMap map) {
        mMap = map;
    }

    private void setUrlNearbyPlace(double latitude , double longitude) {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+ proximity_radius);
        googlePlaceUrl.append("&type=restaurant");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=AIzaSyD5hmxXOnLbZOEwlAuJ5Y8pQHxhPYQ8AvM"); //TODO Check this

        Log.d("MapsActivity", "url = "+googlePlaceUrl.toString());
        this.urlNearbyPlace = googlePlaceUrl.toString();
    }
}
