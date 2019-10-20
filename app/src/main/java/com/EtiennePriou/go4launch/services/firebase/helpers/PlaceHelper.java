package com.EtiennePriou.go4launch.services.firebase.helpers;

import com.EtiennePriou.go4launch.models.Workmate;
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
    private static final String SUBCOLLECTION_FAV = "favorite";


    // --- COLLECTION REFERENCE ---

    private static CollectionReference getFavoriteCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_FAV);
    }

    private static CollectionReference getWhoComingCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_WHO);
    }

    public static DatabaseReference getReference(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createFavorite(String uid, String placeRef) {
        Map<String, Object> toCreateUid = new HashMap<>();
        toCreateUid.put("uid",uid);
        return getFavoriteCollection(placeRef).document(uid).set(toCreateUid);
    }

    public static Task<Void> createWhoComing(String placeRef, String uid) {
        Map<String, Object> toCreate = new HashMap<>();
        toCreate.put("uid",uid);
        return getWhoComingCollection(placeRef).document(uid).set(toCreate);
    }

    // --- GET ---

    public static Task<QuerySnapshot> getPlace(String placeRef){
        return getWhoComingCollection(placeRef).get();
    }

    public static Task<QuerySnapshot> getWhoComing(String placeRef){
        return getWhoComingCollection(placeRef).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUserInFav(String uid, String placeRef) {
        return getFavoriteCollection(placeRef).document(uid).update("uid", uid);
    }

    // --- DELETE ---

    public static Task<Void> deleteUserInFav(String uid, String placeRef) {
        return getFavoriteCollection(placeRef).document(uid).delete();
    }

    public static Task<Void> deleteUserWhoComming(String uid, String placeRef) {
        return getWhoComingCollection(placeRef).document(uid).delete();
    }

    // --- UTIL ---

}
