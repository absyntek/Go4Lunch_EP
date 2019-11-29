package com.EtiennePriou.go4launch.models;

import androidx.annotation.Nullable;

import java.util.Map;

public class Workmate {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private Map<String, Object> placeToGo;

    public Workmate(){ }

    public Workmate(String uid, String username, String urlPicture, Map<String, Object> placeToGo) {
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
    public Map<String, Object> getPlaceToGo() { return placeToGo; }

    // --- SETTERS ---

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlaceToGo(Map<String, Object> placeToGo) {
        this.placeToGo = placeToGo;
    }
}
