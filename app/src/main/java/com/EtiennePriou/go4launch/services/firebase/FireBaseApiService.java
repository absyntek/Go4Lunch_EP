package com.EtiennePriou.go4launch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.EtiennePriou.go4launch.events.ReceiveWorkmatePlace;
import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class FireBaseApiService implements FireBaseApi{

    private List<Workmate> mWorkmates = null;
    private String TAG = getClass().getName();
    private Workmate actualUser = null;
    private FirebaseUser currentUser;


    // ------------- PLACES TO GO ----------------

    public static CollectionReference getPlacesToGoCollection(){
        return FirebaseFirestore.getInstance().collection("placesToGo");
    }

    // --- CREATE ---

    public static Task<Void> createUser(String placeRef, String userRef) {
        return getPlacesToGoCollection().document(placeRef).set(userRef);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getPlacesToGo(String uid){
        return getPlacesToGoCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return getPlacesToGoCollection().document(uid).update("username", username);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return getPlacesToGoCollection().document(uid).delete();
    }







    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void updateWorkmatesList() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    mWorkmates = task.getResult().toObjects(Workmate.class);
                    for (Workmate workmate : mWorkmates){
                        if (workmate.getUid().equals(currentUser.getUid())){
                            actualUser = workmate;
                            mWorkmates.remove(workmate);
                            break;
                        }
                    }
                    EventBus.getDefault().post(new ReceiveWorkmatePlace());

                }else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                    mWorkmates = null;
                }
            }
        });
    }

    @Override
    public List<Workmate> getWorkmatesList() {
        return mWorkmates;
    }

    public Workmate getActualUser() {
        return actualUser;
    }
}
