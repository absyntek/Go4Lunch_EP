package com.EtiennePriou.go4launch.services.places.helpers;

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
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxHeight(200)
                .setMaxWidth(200)
                .build();

        return placesClient.fetchPhoto(photoRequest);
    }

    // -- load image --
    public static Task<FetchPhotoResponse> getPhotoForDetail (Place placeModel, PlacesClient placesClient){

        PhotoMetadata photoMetadata = placeModel.getPhotoMetadatas().get(0);
        FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .build();

        return placesClient.fetchPhoto(photoRequest);
    }

    public static Task<FetchPlaceResponse> getDetails (String placeRef, PlacesClient placesClient){
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.PHONE_NUMBER,
                Place.Field.WEBSITE_URI,
                Place.Field.NAME,
                Place.Field.TYPES,
                Place.Field.ADDRESS,
                Place.Field.ID,
                Place.Field.PHOTO_METADATAS,
                Place.Field.LAT_LNG);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeRef, placeFields);

        return placesClient.fetchPlace(request);
    }

    public static Task<FetchPlaceResponse> getHours (String placeRef, PlacesClient placesClient){
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(
                Place.Field.OPENING_HOURS,
                Place.Field.ID);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeRef, placeFields);

        return placesClient.fetchPlace(request);
    }
}
