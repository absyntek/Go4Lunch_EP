package com.EtiennePriou.go4launch.services.places;


import android.location.Location;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public interface PlacesApi {


    /* --- GETTERS --- */

    void setPlaces(List<Place> places);

    void setLocation(Location location);

    void setPlacesClient(PlacesClient placesClient);

    /* --- SETTERS --- */

    Place getPlaceByReference(String reference);

    List<Place> getPlaces();

    Location getLocation();

    PlacesClient getPlacesClient();
}
