package com.EtiennePriou.go4launch.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PlaceToGo {
    String adresse, placeName, placeRef;
    Date dateCreated;

    public PlaceToGo() {
    }

    public PlaceToGo(String adresse, String placeName, String placeRef) {
        this.adresse = adresse;
        this.placeName = placeName;
        this.placeRef = placeRef;
    }

    //GETTERS

    public String getAdresse() {
        return adresse;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceRef() {
        return placeRef;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

}
