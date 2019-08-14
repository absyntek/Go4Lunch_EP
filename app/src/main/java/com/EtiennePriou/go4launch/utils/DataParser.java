package com.EtiennePriou.go4launch.utils;


import android.util.Log;
import com.EtiennePriou.go4launch.models.Place;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Priyanka
 */

public class DataParser {

    private Place getPlace(JSONObject googlePlaceJson){
        String name = null, adresse = null, reference = null, imgReference = null;
        String placeId = null;
        String lat = null , longit = null;
        String isOpen = null;

        Place place;

        Log.d("DataParser","jsonobject ="+googlePlaceJson.toString());

        try {

            lat = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longit = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            placeId = googlePlaceJson.getString("place_id");

            reference = googlePlaceJson.getString("reference");

            if (!googlePlaceJson.isNull("name")) {
                name = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                adresse = googlePlaceJson.getString("vicinity");
            }
            if (!googlePlaceJson.isNull("opening_hours")){
                isOpen = String.valueOf(googlePlaceJson.getJSONObject("opening_hours").getBoolean("open_now"));
            }

            if (!googlePlaceJson.isNull("photos")){
                imgReference = googlePlaceJson.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        place = new Place(name,adresse,reference,imgReference,placeId,lat,longit,isOpen);
        return place;
    }

    private List<Place>getPlaces(JSONArray jsonArray) {
        int count = jsonArray.length();
        List<Place> placelist = new ArrayList<>();
        Place place;

        for(int i = 0; i<count;i++)
        {
            try {
                place = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public Place addMissingDetails(Place place, JSONObject jsonObject){

        String webSite = null, phone_number = null;
        try {
            if (!jsonObject.isNull("formatted_phone_number")) {
                phone_number = jsonObject.getString("formatted_phone_number");
            }
            if (!jsonObject.isNull("website")) {
                webSite = jsonObject.getString("website");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        place.setWebSite(webSite);
        place.setPhonenumber(phone_number);
        return place;
    }

    public List<Place> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.d("json data", jsonData);

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }
}
