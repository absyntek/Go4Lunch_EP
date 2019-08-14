package com.EtiennePriou.go4launch.services.places;

import com.EtiennePriou.go4launch.models.Place;
import com.EtiennePriou.go4launch.utils.GetNearbyPlacesData;
import com.google.android.gms.maps.GoogleMap;

import java.util.List;

public class DummyPlacesApiService implements PlacesApiService {

    private Integer proximity_radius = 10000;
    private List<Place> mNearbyPlaceList = null;
    private String urlNearbyPlace, urlPlaceDetails;
    GetNearbyPlacesData mGetNearbyPlacesData;


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
        mGetNearbyPlacesData = new GetNearbyPlacesData();
        mGetNearbyPlacesData.execute(this.urlNearbyPlace);
    }

    @Override
    public List<Place> getNearbyPlacesList() {
        return this.mNearbyPlaceList;
    }

    @Override
    public void setNearbyPlaceList(List<Place> nearbyPlaceList) {
        this.mNearbyPlaceList=nearbyPlaceList;
    }

    private void setUrlNearbyPlace(double latitude , double longitude) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+ proximity_radius);
        googlePlaceUrl.append("&type=restaurant");
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=AIzaSyD5hmxXOnLbZOEwlAuJ5Y8pQHxhPYQ8AvM"); //TODO Check this

        this.urlNearbyPlace = googlePlaceUrl.toString();
    }

    private void setUrlPlaceDetails(String placeId){
        StringBuilder placeDetails = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        placeDetails.append("placeid="+placeId);
        placeDetails.append("&fields=formatted_phone_number,opening_hours,website");
        placeDetails.append("&key=AIzaSyD5hmxXOnLbZOEwlAuJ5Y8pQHxhPYQ8AvM");
        this.urlPlaceDetails = placeDetails.toString();
    }
}
