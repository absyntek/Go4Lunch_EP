package com.EtiennePriou.go4launch.services.places;

import android.location.Location;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class PlacesApiService implements PlacesApi {

    private List<Place> mPlaces = null;
    private Location mLocation = null;
    private PlacesClient mPlacesClient;

    // ---- SETTERS ---- //
    @Override
    public void setPlaces(List<Place> places) {
        mPlaces = places;
    }
    @Override
    public void setLocation(Location location) {
        mLocation = location;
    }
    @Override
    public void setPlacesClient(PlacesClient placesClient) {
        mPlacesClient = placesClient;
    }

    // ---- GETTERS ---- //
    @Override
    public Place getPlaceByReference(String reference){
        for (Place placeModel : mPlaces){
            if (placeModel.getId().equals(reference)){
                return placeModel;
            }
        }
        return null;
    }
    @Override
    public List<Place> getPlaces() {
        return mPlaces;
    }
    @Override
    public Location getLocation() {
        return mLocation;
    }
    @Override
    public PlacesClient getPlacesClient() {
        return mPlacesClient;
    }
}
