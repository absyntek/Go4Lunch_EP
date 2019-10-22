package com.EtiennePriou.go4launch.services.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.EtiennePriou.go4launch.events.ReceiveWorkmatePlace;
import com.EtiennePriou.go4launch.models.Workmate;
import com.EtiennePriou.go4launch.services.firebase.helpers.UserHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private FirebaseUser currentUser;

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void setWorkmatesList(List<Workmate> workmates) {
        this.mWorkmates = workmates;
    }

    @Override
    public List<Workmate> getWorkmatesList() {
        return mWorkmates;
    }

    public Workmate getActualUser() {
        return actualUser;
    }
}
