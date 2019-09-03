package com.EtiennePriou.go4launch.utils;


import android.os.AsyncTask;

import com.EtiennePriou.go4launch.models.PlaceModel;

import java.io.IOException;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<String, String, String> {


    private String googlePlacesData;

    @Override
    protected String doInBackground(String... url){

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){

        GetDetailsPlaces mGetDetailsPlaces = new GetDetailsPlaces();

        List<PlaceModel> nearbyPlaceModelList;

        DataParser parser = new DataParser();
        nearbyPlaceModelList = parser.parse(s);

        mGetDetailsPlaces.execute(nearbyPlaceModelList);
    }
}
