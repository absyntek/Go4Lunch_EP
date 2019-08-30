package com.EtiennePriou.go4launch.events;

import com.EtiennePriou.go4launch.models.Places;

import java.util.List;

public class ReceiveListePlace {
    public List<Places> mNearbyPlacesList;

    public ReceiveListePlace (List<Places> nearbyPlacesList){
        this.mNearbyPlacesList = nearbyPlacesList;
    }
}
