package com.EtiennePriou.go4launch.services.utils;

import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class DetailHelper {

    // -- load image --
    public static Task<FetchPhotoResponse> getPhoto (Place placeModel, PlacesClient placesClient){

        PhotoMetadata photoMetadata = placeModel.getPhotoMetadatas().get(0);
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata).build();

        return placesClient.fetchPhoto(photoRequest);
    }

    public static Task<FetchPlaceResponse> getDetails (Place placeModel, PlacesClient placesClient){
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeModel.getId(), placeFields);

        return placesClient.fetchPlace(request);
    }
}
