package com.EtiennePriou.go4launch.utils;

import android.os.AsyncTask;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.events.ReceiveListePlace;
import com.EtiennePriou.go4launch.models.Places;
import com.EtiennePriou.go4launch.services.places.PlacesApiService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GetDetailsPlaces extends AsyncTask<List<Places>, String, List<Places>> {

    @Override
    protected List<Places> doInBackground(List<Places>... lists) {
        List<Places> places = lists[0];
        DataParser parser = new DataParser();
        DownloadURL downloadURL = new DownloadURL();
        for (Places place:places){
            try {
                String googlePlacesDetails = downloadURL.readUrl(place.getUrlPlaceDetails());
                JSONObject jsonObject = new JSONObject(googlePlacesDetails);
                jsonObject = jsonObject.getJSONObject("result");
                place = parser.addMissingDetails(place,jsonObject);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return places;
    }

    @Override
    protected void onPostExecute(List<Places> places) {
        PlacesApiService mPlacesApiService = DI.getServiceApiPlaces();
        mPlacesApiService.setNearbyPlacesList(places);
        EventBus.getDefault().post(new ReceiveListePlace(places));

    }
}
