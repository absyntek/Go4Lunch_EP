package com.EtiennePriou.go4launch.services.places;

import com.EtiennePriou.go4launch.BuildConfig;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.utils.GetNearbyPlacesData;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class PlacesApiService implements PlacesApi {



    private Integer proximity_radius = 10000;
    private List<PlaceModel> mNearbyPlaceModelList = null;
    private String urlNearbyPlace;

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

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+ proximity_radius);
        googlePlaceUrl.append("&type=restaurant");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + BuildConfig.PlaceApiKey); //TODO Check this

        this.urlNearbyPlace = googlePlaceUrl.toString();
    }
    @Override
    public void setUrlPlaceDetails(String placeId){
        StringBuilder placeDetails = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        placeDetails.append("placeid="+placeId);
        placeDetails.append("&fields=formatted_phone_number,opening_hours,website");
        placeDetails.append("&key=" + BuildConfig.PlaceApiKey);

        String urlPlaceDetails = placeDetails.toString();
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

    // ---- UTLIS ---- //


}
