package com.EtiennePriou.go4launch.events;

import com.EtiennePriou.go4launch.models.Place;

import java.util.List;

public class ReceiveListePlace {
    public List<Place> nearbyPlaceList;

    public ReceiveListePlace (List<Place> nearbyPlaceList){
        this.nearbyPlaceList = nearbyPlaceList;
    }
}
