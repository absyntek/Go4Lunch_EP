package com.EtiennePriou.go4launch.utils;

import android.os.AsyncTask;

import com.EtiennePriou.go4launch.di.DI;
import com.EtiennePriou.go4launch.events.ReceiveListPlace;
import com.EtiennePriou.go4launch.models.PlaceModel;
import com.EtiennePriou.go4launch.services.places.PlacesApi;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class GetDetailsPlaces extends AsyncTask<List<PlaceModel>, String, List<PlaceModel>> {

    @Override
    protected List<PlaceModel> doInBackground(List<PlaceModel>... lists) {
        List<PlaceModel> places = lists[0];
        DataParser parser = new DataParser();
        DownloadURL downloadURL = new DownloadURL();
        for (PlaceModel place:places){
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
    protected void onPostExecute(List<PlaceModel> places) {
        PlacesApi mPlacesApi = DI.getServiceApiPlaces();
        mPlacesApi.setNearbyPlaceModelList(places);
        EventBus.getDefault().post(new ReceiveListPlace());

    }
}
