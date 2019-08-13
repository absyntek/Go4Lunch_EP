package com.EtiennePriou.go4launch.di;

import com.EtiennePriou.go4launch.services.DummyPlacesApiService;
import com.EtiennePriou.go4launch.services.PlacesApiService;

public class DI {
    private static PlacesApiService servicePlaces = new DummyPlacesApiService();

    public static PlacesApiService getServiceApiPlaces(){
        return servicePlaces;
    }
}
