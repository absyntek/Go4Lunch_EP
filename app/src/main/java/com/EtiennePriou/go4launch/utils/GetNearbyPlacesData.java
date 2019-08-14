package com.EtiennePriou.go4launch.utils;


import android.os.AsyncTask;

import com.EtiennePriou.go4launch.models.Place;

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

        List<Place> nearbyPlaceList;

        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);

        mGetDetailsPlaces.execute(nearbyPlaceList);
    }
}
