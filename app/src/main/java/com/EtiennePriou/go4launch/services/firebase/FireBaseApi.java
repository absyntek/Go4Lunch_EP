package com.EtiennePriou.go4launch.services.firebase;

import com.EtiennePriou.go4launch.models.Workmate;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface FireBaseApi {

    /*
     GETTERS
     */
    List<Workmate> getWorkmatesList ();
    List<Workmate> getWorkmatesListNoMe ();
    Workmate getActualUser();
    FirebaseUser getCurrentUser();

    /*
    SETTERS
    */
    void setWorkmatesList(List<Workmate> workmates);
    void setCurrentUser(FirebaseUser currentUser);
    void setActualUser (Workmate actualUser);
}
