package com.EtiennePriou.go4launch.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class WhoComing {
    String name, uid;
    Date dateCreated;

    public WhoComing() {
    }

    public WhoComing(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    @ServerTimestamp
    public Date getDateCreated() {
        return dateCreated;
    }

    // SETTERS


    public void setName(String name) {
        this.name = name;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
