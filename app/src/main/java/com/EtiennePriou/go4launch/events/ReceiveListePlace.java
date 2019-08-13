package com.EtiennePriou.go4launch.events;

import java.util.HashMap;
import java.util.List;

public class ReceiveListePlace {
    public List<HashMap<String, String>> nearbyPlaceList;

    public ReceiveListePlace (List<HashMap<String, String>> nearbyPlaceList){
        this.nearbyPlaceList = nearbyPlaceList;
    }
}
