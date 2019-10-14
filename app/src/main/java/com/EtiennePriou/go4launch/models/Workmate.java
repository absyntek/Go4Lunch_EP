package com.EtiennePriou.go4launch.models;

import androidx.annotation.Nullable;

public class Workmate {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String placeToGo;

    public Workmate(){ }

    public Workmate(String uid, String username, String urlPicture, String placeToGo) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.placeToGo = placeToGo;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    @Nullable
    public String getUrlPicture() { return urlPicture; }
    public String getPlaceToGo() { return placeToGo; }


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setPlaceToGo(String placeToGo) { this.placeToGo = placeToGo; }

}
