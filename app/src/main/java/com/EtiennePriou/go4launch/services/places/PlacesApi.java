package com.EtiennePriou.go4launch.services.places;


import android.location.Location;

import com.EtiennePriou.go4launch.models.PlaceModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public interface PlacesApi {


    /*
     GETTERS
     */
    void setProximity_radius(Integer proximity_radius);

    void setListPlaces(double latitude, double longitude, GoogleMap map);

    void setNearbyPlaceModelList(List<PlaceModel> nearbyPlaceModelList);

    void setUrlNearbyPlace(double latitude , double longitude);

    void setPlaces(List<Place> places);

    void setLocation(Location location);

    void setPlacesClient(PlacesClient placesClient);

    /*
     SETTERS
     */
    Integer getProximity_radius();

    List<PlaceModel> getNearbyPlaceModelList();

    Place getPlaceByReference(String reference);

    String getUrlNearbyPlace();

    List<Place> getPlaces();

    Location getLocation();

    PlacesClient getPlacesClient();
}
