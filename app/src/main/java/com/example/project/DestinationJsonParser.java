package com.example.project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DestinationJsonParser {
    public static List<TravelDestination> getObjectFromJson(String json) {
        List<TravelDestination> travelDestinations;
        try {
            JSONArray jsonArray = new JSONArray(json);
            travelDestinations = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject = (JSONObject) jsonArray.get(i);
                TravelDestination td = new TravelDestination();
                td.setId(i);
                td.setCity(jsonObject.getString("city"));
                td.setCountry(jsonObject.getString("country"));
                td.setContinent(jsonObject.getString("continent"));
                td.setLongitude(jsonObject.getDouble("longitude"));
                td.setLatitude(jsonObject.getDouble("latitude"));
                td.setCost(jsonObject.getInt("cost"));
                td.setImg(jsonObject.getString("img"));
                td.setDescription(jsonObject.getString("description"));
                travelDestinations.add(td);
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return travelDestinations;
    }
}
