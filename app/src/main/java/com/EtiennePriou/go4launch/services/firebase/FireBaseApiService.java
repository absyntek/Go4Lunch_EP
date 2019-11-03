package com.EtiennePriou.go4launch.services.firebase;

import com.EtiennePriou.go4launch.models.Workmate;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FireBaseApiService implements FireBaseApi{

    private List<Workmate> mWorkmates = null, mWorkmatesNoMe;
    private Workmate actualUser = null;
    private FirebaseUser currentUser;

    /*
     GETTERS
     */
    @Override
    public FirebaseUser getCurrentUser() {
        return currentUser;
    }
    @Override
    public List<Workmate> getWorkmatesList() {
        return mWorkmates;
    }
    @Override
    public List<Workmate> getWorkmatesListNoMe() { return mWorkmatesNoMe; }
    @Override
    public Workmate getActualUser() {
        return actualUser;
    }


    /*
     SETTERS
     */
    @Override
    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }
    @Override
    public void setActualUser(Workmate actualUser) {
        this.actualUser = actualUser;
    }
    @Override
    public void setWorkmatesList(List<Workmate> workmates) {
        this.mWorkmates = workmates;
    }
    @Override
    public void setWorkmateListNoMe (String uid){
        mWorkmatesNoMe = new ArrayList<>();
        for (Workmate workmate : mWorkmates){
            if (!workmate.getUid().equals(uid)){
                mWorkmatesNoMe.add(workmate);
            }
        }
    }
}
