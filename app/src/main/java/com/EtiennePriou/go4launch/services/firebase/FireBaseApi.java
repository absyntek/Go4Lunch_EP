package com.EtiennePriou.go4launch.services.firebase;

import com.EtiennePriou.go4launch.models.Workmate;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public interface FireBaseApi {

    void updateWorkmatesList(FirebaseUser currentUser);
    List<Workmate> getWorkmatesList ();
    Workmate getActualUser();
}
