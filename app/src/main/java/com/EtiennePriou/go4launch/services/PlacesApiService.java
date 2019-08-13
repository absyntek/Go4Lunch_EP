package com.EtiennePriou.go4launch.services;

import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.List;

public interface PlacesApiService {

    void setProximity_radius(Integer proximity_radius);

    Integer getProximity_radius();

    void setListPlaces(double latitude, double longitude, GoogleMap map);

    List<HashMap<String, String>> getNearbyPlacesList();

    void setNearbyPlaceList(List<HashMap<String, String>> nearbyPlaceList);
}
