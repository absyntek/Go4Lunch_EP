package com.EtiennePriou.go4launch.services.places;

import com.EtiennePriou.go4launch.models.Place;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public interface PlacesApiService {

    void setProximity_radius(Integer proximity_radius);

    Integer getProximity_radius();

    void setListPlaces(double latitude, double longitude, GoogleMap map);

    List<Place> getNearbyPlacesList();

    void setNearbyPlaceList(List<Place> nearbyPlaceList);
}
