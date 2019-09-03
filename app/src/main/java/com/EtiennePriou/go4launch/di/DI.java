package com.EtiennePriou.go4launch.di;

import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApiService;
import com.EtiennePriou.go4launch.services.map.GoogleMapApiService;
import com.EtiennePriou.go4launch.services.map.GoogleMapApi;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApi;

public class DI {
    private static PlacesApi servicePlaces = new PlacesApiService();
    private static GoogleMapApi serviceMap = new GoogleMapApiService();
    private static FireBaseApi serviceFireBase = new FireBaseApiService();

    public static PlacesApi getServiceApiPlaces(){
        return servicePlaces;
    }
    public static GoogleMapApi getServiceApiMap(){
        return serviceMap;
    }
    public static FireBaseApi getServiceFireBase() {
        return serviceFireBase;
    }
}
