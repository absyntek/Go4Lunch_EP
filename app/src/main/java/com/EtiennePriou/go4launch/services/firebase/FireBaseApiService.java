package com.EtiennePriou.go4launch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.EtiennePriou.go4launch.events.ReceiveWorkmatePlace;
import com.EtiennePriou.go4launch.models.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class FireBaseApiService implements FireBaseApi{

    private List<Workmate> mWorkmates = null;
    private String TAG = getClass().getName();
    private Workmate actualUser = null;

    @Override
    public void updateWorkmatesList(final FirebaseUser currentUser) {
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
