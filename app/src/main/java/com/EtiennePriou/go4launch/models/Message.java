package com.EtiennePriou.go4launch.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    private String message;
    private Date dateCreated;
    private Workmate userSender;
    private String urlImage;

    public Message() { }

    public Message(String message, Workmate userSender) {
        this.message = message;
        this.userSender = userSender;
    }

    public Message(String message, String urlImage, Workmate userSender) {
        this.message = message;
        this.urlImage = urlImage;
        this.userSender = userSender;
    }

    public Message(String message, Date dateCreated, Workmate userSender, String urlImage) {
        this.message = message;
        this.dateCreated = dateCreated;
        this.userSender = userSender;
        this.urlImage = urlImage;
    }

    // --- GETTERS ---
    public String getMessage() { return message; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }
    public Workmate getUserSender() { return userSender; }
    public String getUrlImage() { return urlImage; }

    // --- SETTERS ---
    public void setMessage(String message) { this.message = message; }
    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }
    public void setUserSender(Workmate userSender) { this.userSender = userSender; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
}