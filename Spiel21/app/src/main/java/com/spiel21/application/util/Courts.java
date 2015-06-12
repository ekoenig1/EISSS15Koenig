package com.spiel21.application.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Courts {


    // Konvertiert JSON in Klassen, http://json2csharp.com/
    private String _id;
    private String name;
    private String street;
    private String code;
    private String city;
    private String country;
    private String latitude;
    private String longitude;


    private static Gson gson = new Gson();

    public Courts() {
    }

    public static Courts createCourt(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, Courts.class);
    }


    public static ArrayList<Courts> createCourtList(String json) {
        ArrayList<Courts> liste = gson.fromJson(json,
                new TypeToken<ArrayList<Courts>>() {
                }.getType());
        return liste;
    }


    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getStr() {
        return street;
    }

    public String getPlz() {
        return code;
    }

    public String getStadt() {
        return city;
    }

    public String getLand() {
        return country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


}
