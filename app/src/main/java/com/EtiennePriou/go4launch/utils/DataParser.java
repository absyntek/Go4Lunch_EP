package com.EtiennePriou.go4launch.utils;


import android.util.Log;
import com.EtiennePriou.go4launch.models.Places;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Priyanka
 */

public class DataParser {

    private Places getPlace(JSONObject googlePlaceJson){
        String name = null, adresse = null, reference = null, imgReference = null;
        String placeId = null;
        String lat = null , longit = null;
        String isOpen = null;

        Places places;

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

        places = new Places(name,adresse,reference,imgReference,placeId,lat,longit,isOpen);
        return places;
    }

    private List<Places>getPlaces(JSONArray jsonArray) {
        int count = jsonArray.length();
        List<Places> placelist = new ArrayList<>();
        Places places;

        for(int i = 0; i<count;i++)
        {
            try {
                places = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(places);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    public Places addMissingDetails(Places places, JSONObject jsonObject){

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
        places.setWebSite(webSite);
        places.setPhonenumber(phone_number);
        return places;
    }

    public List<Places> parse(String jsonData) {
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
