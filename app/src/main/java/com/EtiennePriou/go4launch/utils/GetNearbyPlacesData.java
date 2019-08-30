package com.EtiennePriou.go4launch.utils;


import android.os.AsyncTask;

import com.EtiennePriou.go4launch.models.Places;

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

        List<Places> nearbyPlacesList;

        DataParser parser = new DataParser();
        nearbyPlacesList = parser.parse(s);

        mGetDetailsPlaces.execute(nearbyPlacesList);
    }
}
