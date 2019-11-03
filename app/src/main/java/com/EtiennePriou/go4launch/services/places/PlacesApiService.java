package com.EtiennePriou.go4launch.services.places;

import com.EtiennePriou.go4launch.BuildConfig;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.utils.GetNearbyPlacesData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class PlacesApiService implements PlacesApi {



    private Integer proximity_radius = 10000;
    private List<PlaceModel> mNearbyPlaceModelList = null;
    private String urlNearbyPlace;
    PlacesClient mPlacesClient;

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
    public PlaceModel getPlaceByReference(String reference){
        for (PlaceModel placeModel : mNearbyPlaceModelList){
            if (placeModel.getReference().equals(reference)){
                return placeModel;
            }
        }
        return null;
    }
    @Override
    public String getUrlNearbyPlace() {
        return urlNearbyPlace;
    }


}
