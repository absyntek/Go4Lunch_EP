package com.EtiennePriou.go4launch.di;

import com.EtiennePriou.go4launch.services.firebase.FireBaseApi;
import com.EtiennePriou.go4launch.services.firebase.FireBaseApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;
import com.EtiennePriou.go4launch.services.places.PlacesApi;

public class DI {
    private static PlacesApi servicePlaces = new PlacesApiService();
    private static FireBaseApi serviceFireBase = new FireBaseApiService();
    private static ViewModelFactory sModelFactory = new ViewModelFactory();

    public static PlacesApi getServiceApiPlaces(){
        return servicePlaces;
    }
    public static FireBaseApi getServiceFireBase() {
        return serviceFireBase;
    }
    public static ViewModelFactory provideViewModelFactory () { return sModelFactory; }
}
