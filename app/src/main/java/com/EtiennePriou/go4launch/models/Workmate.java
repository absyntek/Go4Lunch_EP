package com.EtiennePriou.go4launch.models;

import androidx.annotation.Nullable;

public class Workmate {
    private String uid;
    private String username;
    @Nullable
    private String urlPicture;

    public Workmate() { }

    public Workmate(String uid, String username, String urlPicture) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
}
