package com.EtiennePriou.go4launch.services.firebase.helpers;

import com.EtiennePriou.go4launch.models.PlaceToGo;
import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    private static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(Workmate userToCreate) {
        return UserHelper.getUsersCollection().document(userToCreate.getUid()).set(userToCreate);
    }

    // --- GET ---

    public static Task<QuerySnapshot> getUserList(){
        return UserHelper.getUsersCollection().get();
    }

    public static Query getUsers(){
        return getUsersCollection().orderBy("username", Query.Direction.ASCENDING);
    }

    public static Query getUsersSearch(String search){
        return getUsersCollection().whereGreaterThanOrEqualTo("username",search).whereLessThanOrEqualTo("username",search + "z");
    }

    public static Task<DocumentSnapshot> getUser(String uid){
        return UserHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updatePlaceToGo(String uid, PlaceToGo placeToGo) {
        return UserHelper.getUsersCollection().document(uid).update("placeToGo", placeToGo);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }
}