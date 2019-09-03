package com.EtiennePriou.go4launch.models;

import androidx.annotation.Nullable;

import java.util.List;

public class Workmate {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String placeToGo;
    private List<String> favoritePlaces;

    public Workmate(){ }

    public Workmate(String uid, String username, String urlPicture, String placeToGoRef, List<String> favoritePlaces) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.placeToGo = placeToGoRef;
        this.favoritePlaces = favoritePlaces;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getPlaceToGo(){ return placeToGo; }
    public List<String> getFavoritePlaces() { return favoritePlaces; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setPlaceToGo (String placeToGoRef) { this.placeToGo = placeToGoRef; }
    public void setFavoritePlaces(List<String> favoritePlaces) { this.favoritePlaces = favoritePlaces; }
}
