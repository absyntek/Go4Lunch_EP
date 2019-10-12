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
    private static final String SUBCOLLECTION_NAME = "whoComing";


    // --- COLLECTION REFERENCE ---

    private static CollectionReference getPlacesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private static CollectionReference getWhoComingCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_NAME);
    }

    public static DatabaseReference getReference(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createPlace(String placeRef) {
        return getPlacesCollection().document(placeRef).set(placeRef);
    }

    public static Task<Void> createWhoComing(String placeRef, Object userRef) {
        Map<String, Object> toCreate = new HashMap<>();
        toCreate.put("userRef",userRef);
        return getWhoComingCollection(placeRef).document(userRef.toString()).set(toCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getPlace(String placeRef){
        return getPlacesCollection().document(placeRef).get();
    }

    public static Task<QuerySnapshot> getWhoComing(String placeRef){
        return getWhoComingCollection(placeRef).get();
    }

    // --- UPDATE ---

    public static Task<Void> updatePlace(String placeRef) {
        return getPlacesCollection().document(placeRef).update("placeRef", placeRef);
    }

    // --- DELETE ---

    public static Task<Void> deletePlace(String uid) {
        return getPlacesCollection().document(uid).delete();
    }

    public static Task<Void> deleteUserWhoComming(String placeRef, String userRef) {
        return getWhoComingCollection(placeRef).document(userRef).delete();
    }
}
