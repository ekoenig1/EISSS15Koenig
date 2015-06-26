package com.spiel21.application.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Courts2 {


    // Konvertiert JSON in Klassen, http://json2csharp.com/
    private String courtname;
    private String date;
    private String time;


    private static Gson gson = new Gson();

    public Courts2() {
    }

    public static Courts2 createCourt(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, Courts2.class);
    }


    public static ArrayList<Courts2> createCourts2List(String json) {
        ArrayList<Courts2> liste = gson.fromJson(json,
                new TypeToken<ArrayList<Courts2>>() {
                }.getType());
        return liste;
    }

    public String getName() {
        return courtname;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
