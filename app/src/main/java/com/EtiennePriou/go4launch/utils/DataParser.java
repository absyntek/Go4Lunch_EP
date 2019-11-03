package com.EtiennePriou.go4launch.utils;


import android.util.Log;

import com.EtiennePriou.go4launch.models.PlaceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Priyanka
 */

class DataParser {

    private PlaceModel getPlace(JSONObject googlePlaceJson){
        String name = null, adresse = null, reference = null, imgReference = null;
        String placeId = null;
        String lat = null , longit = null;
        String isOpen = null;

        PlaceModel placeModel;

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

        placeModel = new PlaceModel(name,adresse,reference,imgReference,placeId,lat,longit,isOpen);
        return placeModel;
    }

    private List<PlaceModel>getPlaces(JSONArray jsonArray) {
        int count = jsonArray.length();
        List<PlaceModel> placelist = new ArrayList<>();
        PlaceModel placeModel;

        for(int i = 0; i<count;i++)
        {
            try {
                placeModel = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }

    PlaceModel addMissingDetails(PlaceModel placeModel, JSONObject jsonObject){

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
        placeModel.setWebSite(webSite);
        placeModel.setPhonenumber(phone_number);
        return placeModel;
    }

    List<PlaceModel> parse(String jsonData) {
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
