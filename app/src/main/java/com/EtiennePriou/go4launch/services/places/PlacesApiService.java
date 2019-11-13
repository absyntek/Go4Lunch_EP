package com.EtiennePriou.go4launch.services.places;

import android.location.Location;

import com.EtiennePriou.go4launch.BuildConfig;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.utils.GetNearbyPlacesData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class PlacesApiService implements PlacesApi {



    private Integer proximity_radius = 10000;
    private List<PlaceModel> mNearbyPlaceModelList = null;
    private List<Place> mPlaces = null;
    private String urlNearbyPlace;
    private Location mLocation = null;
    private PlacesClient mPlacesClient;

    // ---- SETTERS ---- //
    @Override
    public void setProximity_radius(Integer proximity_radius) {
        this.proximity_radius = proximity_radius;
    }
    @Override
    public void setListPlaces(double latitude, double longitude, GoogleMap map) {
        setUrlNearbyPlace(latitude,longitude);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(this.urlNearbyPlace);
    }
    @Override
    public void setNearbyPlaceModelList(List<PlaceModel> nearbyPlaceModelList) {
        this.mNearbyPlaceModelList = nearbyPlaceModelList;
    }
    @Override
    public void setUrlNearbyPlace(double latitude , double longitude) {

        String googlePlaceUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + latitude + "," + longitude +
                "&radius=" + proximity_radius +
                "&type=restaurant" +
                "&sensor=true" +
                "&key=" + BuildConfig.PlaceApiKey;
        this.urlNearbyPlace = googlePlaceUrl;
    }
    @Override
    public void setPlaces(List<Place> places) {
        mPlaces = places;
    }
    @Override
    public void setLocation(Location location) {
        mLocation = location;
    }

    public void setPlacesClient(PlacesClient placesClient) {
        mPlacesClient = placesClient;
    }

    // ---- GETTERS ---- //
    @Override
    public Integer getProximity_radius() {
        return this.proximity_radius;
    }
    @Override
    public List<PlaceModel> getNearbyPlaceModelList() {
        return this.mNearbyPlaceModelList;
    }
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
    public String getUrlNearbyPlace() {
        return urlNearbyPlace;
    }
    @Override
    public List<Place> getPlaces() {
        return mPlaces;
    }
    @Override
    public Location getLocation() {
        return mLocation;
    }

    public PlacesClient getPlacesClient() {
        return mPlacesClient;
    }
}
