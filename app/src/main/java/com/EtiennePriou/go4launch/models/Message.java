package com.EtiennePriou.go4launch.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Message {

    /**
     *  Message to show
     **/
    private String message;

    /**
     * Server creation timestamp
     **/
    private Date dateCreated;

    /**
     * Workmate model who send this message
     */
    private Workmate userSender;

    public Message() { }

    public Message(String message, Workmate userSender) {
        this.message = message;
        this.userSender = userSender;
    }

    // --- GETTERS ---
    public String getMessage() { return message; }
    @ServerTimestamp
    public Date getDateCreated() { return dateCreated; }
    public Workmate getUserSender() { return userSender; }
}