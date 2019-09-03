package com.EtiennePriou.go4launch.models;

import android.net.Uri;

public class PlaceModel {

    private String name, adresse, reference, imgReference;
    private String placeId;
    private String lat,longit;
    private String webSite,phonenumber;
    private String isOpen;

    public PlaceModel(String name, String adresse, String reference, String imgReference, String placeId, String lat, String longit, String isOpen) {
        this.name = name;
        this.adresse = adresse;
        this.reference = reference;
        this.imgReference = imgReference;
        this.placeId = placeId;
        this.lat = lat;
        this.longit = longit;
        this.isOpen = isOpen;
    }

    // ---- GETTERS ----
    public String getName() {
        return name;
    }
    public String getAdresse() {
        return adresse;
    }
    public String getLat() {
        return lat;
    }
    public String getLongit() {
        return longit;
    }
    public String getReference() {
        return reference;
    }
    public String getImgReference() {
        return imgReference;
    }
    public Uri getPhotoUri (){
        Uri photoUri;
        photoUri = Uri.parse("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+this.imgReference+"&key=AIzaSyD5hmxXOnLbZOEwlAuJ5Y8pQHxhPYQ8AvM");
        return photoUri;
    }
    public String isOpen() {
        return this.isOpen;
    }
    public String getUrlPlaceDetails(){
        StringBuilder placeDetails = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        placeDetails.append("placeid="+ this.placeId);
        placeDetails.append("&fields=formatted_phone_number,opening_hours,website");
        placeDetails.append("&key=AIzaSyD5hmxXOnLbZOEwlAuJ5Y8pQHxhPYQ8AvM");
        return placeDetails.toString();
    }
    public String getWebSite() {
        return webSite;
    }
    public String getPhonenumber() {
        return phonenumber;
    }


    // --- SETTERS ---

    public void setName(String name) {
        this.name = name;
    }
    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
