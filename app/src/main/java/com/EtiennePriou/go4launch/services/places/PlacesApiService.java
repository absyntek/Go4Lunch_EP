package com.EtiennePriou.go4launch.services.places;

import com.EtiennePriou.go4launch.models.Places;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public interface PlacesApiService {



    void setProximity_radius(Integer proximity_radius);

    void setListPlaces(double latitude, double longitude, GoogleMap map);

    void setNearbyPlacesList(List<Places> nearbyPlacesList);

    void setUrlNearbyPlace(double latitude , double longitude);

    void setUrlPlaceDetails(String placeId);



    Integer getProximity_radius();

    List<Places> getNearbyPlacesList();

    Places getPlaceByReference(String reference);
}
