package com.EtiennePriou.go4launch.services.firebase.helpers;

import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";
    private static final String SUBCOLLECTION_FAV = "favorite";

    // --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    private static CollectionReference getFavoriteCollection(String uid){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(uid).collection(SUBCOLLECTION_FAV);
    }

    public static DatabaseReference getReference(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        List<String> favoritePlaces = new ArrayList<>();
        Workmate userToCreate = new Workmate(uid, username, urlPicture, null);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Task<Void> createUserFav(String placeRef, String uid, String placeName) {
        Map<String, Object> toCreate = new HashMap<>();
        toCreate.put("placeRef",placeRef);
        toCreate.put("placeName",placeName);
        return UserHelper.getUsersCollection().document(uid).set(toCreate);
    }

    // --- GET ---

    public static Task<QuerySnapshot> getUserList(){
        return UserHelper.getUsersCollection().get();
    }

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getMyFav(String uid){
        return UserHelper.getFavoriteCollection(uid).get();
    }

    public static Task<DocumentSnapshot> getSpecFavExist(String uid, String placeRef){
        return UserHelper.getFavoriteCollection(uid).document(placeRef).get(); //TODO change code
    }

    public static Query queryUser(String searchQuery){
        return getUsersCollection().whereArrayContains("username",searchQuery);
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updatePlaceToGo(String uid, Map<String, Object> placeRef) {
        return UserHelper.getUsersCollection().document(uid).update("placeToGo", placeRef);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

    public static Task<Void> deleteFav(String uid, String placeRef) {
        return UserHelper.getFavoriteCollection(uid).document(placeRef).delete();
    }

}