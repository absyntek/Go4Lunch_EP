package com.EtiennePriou.go4launch.services.firebase.helpers;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class PlaceHelper {

    private static final String COLLECTION_NAME = "Places";
    private static final String SUBCOLLECTION_WHO = "whoComing";
    private static final String SUBCOLLECTION_NOTE = "note";


    // --- COLLECTION REFERENCE ---

    /**
     * reference for Place Notes on Firestore
     * @param placeRef
     * @return
     */
    private static CollectionReference getNoteCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_NOTE);
    }

    /**
     * reference for Workemate who comming on spécific place on Firestore
     * @param placeRef
     * @return
     */
    private static CollectionReference getWhoComingCollection(String placeRef){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(placeRef).collection(SUBCOLLECTION_WHO);
    }

    // --- CREATE ---

    /**
     * Create note for specific place on Firestore
     * @param uid
     * @param placeRef
     * @param note
     * @return
     */
    public static Task<Void> createNote(String uid, String placeRef, int note) {
        Map<String, Object> noteToCreate = new HashMap<>();
        noteToCreate.put("uid",uid);
        noteToCreate.put("note", note);
        return getNoteCollection(placeRef).document(uid).set(noteToCreate);
    }

    /**
     * Add actual User who wanna come to a specific Place
     * @param placeRef
     * @param whoComing
     * @return
     */
    public static Task<Void> createWhoComing(String placeRef, Workmate whoComing) {
        return getWhoComingCollection(placeRef).document(whoComing.getUid()).set(whoComing);
    }

    // --- GET ---

    /**
     * return the List of users who wanna come to specific place
     * @param placeRef
     * @return
     */
    public static Task<QuerySnapshot> getWhoComing(String placeRef){
        return getWhoComingCollection(placeRef).get();
    }

    /**
     * Live query the List of users who wanna come to specific place today
     * @param placeRef
     * @return
     */
    public static Query getUsersToday(String placeRef){
        Date today = generateTodayNoHours();
        return getWhoComingCollection(placeRef).orderBy("dateCreated", Query.Direction.ASCENDING).whereGreaterThan("dateCreated",today);
    }

    /**
     * Return the list of notes given to a specific place
     * wich is use for calculate the average
     * @param placeRef
     * @return
     */
    public static Task<QuerySnapshot> getNotes (String placeRef){
        return getNoteCollection(placeRef).get();
    }

    /**
     * return the note for a specific place that the actual user given
     * @param placeRef
     * @param uid
     * @return
     */
    public static Task<DocumentSnapshot> getMyNote(String placeRef, String uid) {
        return getNoteCollection(placeRef).document(uid).get();
    }

    // --- DELETE ---

    /**
     * Delete the actual user from whoComingList for a specific place
     * @param uid
     * @param placeRef
     * @return
     */
    public static Task<Void> deleteUserWhoComming(String uid, String placeRef) {
        return getWhoComingCollection(placeRef).document(uid).delete();
    }

    // --- UTILS ---

    /**
     * return the actual date at 1AM
     * @return
     */
    private static Date generateTodayNoHours() {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy" , Locale.FRANCE);
        DateFormat format2 = new SimpleDateFormat("dd.MM.yyyy.HH.mm" , Locale.FRANCE);
        String s = format.format(date);
        s = s + ".01.00";
        Date trimmed = null;
        try {
            trimmed = format2.parse(s);
        } catch (ParseException ignored) {}
        return trimmed;
    }
}
