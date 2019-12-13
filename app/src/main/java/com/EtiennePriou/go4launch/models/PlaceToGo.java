package com.EtiennePriou.go4launch.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class PlaceToGo {
    private String adresse, placeName, placeRef;
    private Date dateCreated;

    public PlaceToGo() {
    }

    public PlaceToGo(String adresse, String placeName, String placeRef) {
        this.adresse = adresse;
        this.placeName = placeName;
        this.placeRef = placeRef;
    }

    public PlaceToGo(String adresse, String placeName, String placeRef, Date dateCreated) {
        this.adresse = adresse;
        this.placeName = placeName;
        this.placeRef = placeRef;
        this.dateCreated = dateCreated;
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
