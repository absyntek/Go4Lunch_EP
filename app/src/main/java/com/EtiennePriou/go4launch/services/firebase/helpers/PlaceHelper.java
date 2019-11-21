package com.EtiennePriou.go4launch.services.firebase.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class PlaceHelper {

    private static final String COLLECTION_NAME = "Places";
    private static final String SUBCOLLECTION_WHO = "whoComing";
    private static final String SUBCOLLECTION_NOTE = "note";


    // --- COLLECTION REFERENCE ---

    private static CollectionReference getNoteCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_NOTE);
    }

    private static CollectionReference getWhoComingCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_WHO);
    }

    public static DatabaseReference getReference(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createFavorite(String uid, String placeRef, int note) {
        Map<String, Object> noteToCreate = new HashMap<>();
        noteToCreate.put("uid",uid);
        noteToCreate.put("note", note);
        return getNoteCollection(placeRef).document(uid).set(noteToCreate);
    }

    public static Task<Void> createWhoComing(String placeRef, String uid, String username) {
        Map<String, Object> toCreate = new HashMap<>();
        toCreate.put("uid",uid);
        toCreate.put("name", username);
        return getWhoComingCollection(placeRef).document(uid).set(toCreate);
    }

    // --- GET ---

    public static Task<QuerySnapshot> getPlace(String placeRef){
        return getWhoComingCollection(placeRef).get();
    }

    public static Task<QuerySnapshot> getWhoComing(String placeRef){
        return getWhoComingCollection(placeRef).get();
    }

    public static Task<QuerySnapshot> getNotes (String placeRef){
        return getNoteCollection(placeRef).get();
    }

    public static Task<DocumentSnapshot> getMyNote(String placeRef, String uid) {
        return getNoteCollection(placeRef).document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateNote(String uid, String placeRef, int note) {
        return getNoteCollection(placeRef).document(uid).update("note", note);
    }

    // --- DELETE ---

    public static Task<Void> deleteUserWhoComming(String uid, String placeRef) {
        return getWhoComingCollection(placeRef).document(uid).delete();
    }

    // --- UTIL ---

}
