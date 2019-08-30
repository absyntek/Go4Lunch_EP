package com.EtiennePriou.go4launch.di;

import com.EtiennePriou.go4launch.services.map.DummyGoogleMapApiService;
import com.EtiennePriou.go4launch.services.map.GoogleMapApiService;
import com.EtiennePriou.go4launch.services.places.RealPlacesApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;

public class DI {
    private static PlacesApiService servicePlaces = new RealPlacesApiService();
    private static GoogleMapApiService serviceMap = new DummyGoogleMapApiService();

    public static PlacesApiService getServiceApiPlaces(){
        return servicePlaces;
    }
    public static GoogleMapApiService getServiceApiMap(){
        return serviceMap;
    }

}
