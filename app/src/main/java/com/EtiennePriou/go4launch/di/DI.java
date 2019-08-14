package com.EtiennePriou.go4launch.di;

import com.EtiennePriou.go4launch.services.map.DummyGoogleMapApiService;
import com.EtiennePriou.go4launch.services.map.GoogleMapApiService;
import com.EtiennePriou.go4launch.services.places.DummyPlacesApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;

public class DI {
    private static PlacesApiService servicePlaces = new DummyPlacesApiService();
    private static GoogleMapApiService serviceMap = new DummyGoogleMapApiService();

    public static PlacesApiService getServiceApiPlaces(){
        return servicePlaces;
    }
    public static GoogleMapApiService getServiceApiMap(){
        return serviceMap;
    }

}
