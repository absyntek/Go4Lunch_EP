package com.EtiennePriou.go4launch.di;

import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApi;

public class DI {
    private static PlacesApi servicePlaces = new PlacesApiService();
    private static FireBaseApi serviceFireBase = new FireBaseApiService();

    public static PlacesApi getServiceApiPlaces(){
        return servicePlaces;
    }
    public static FireBaseApi getServiceFireBase() {
        return serviceFireBase;
    }
}
