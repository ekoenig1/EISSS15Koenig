package com.spiel21.application.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class PlayerResult {


    // Konvertiert JSON in Klassen, http://json2csharp.com/
    private String username;
    private String win;
    private String lose;

    private static Gson gson = new Gson();

    public PlayerResult() {
    }

    public static PlayerResult createPlayerResult(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, PlayerResult.class);
    }


    public static ArrayList<PlayerResult> createPlayerResultList(String json) {
        ArrayList<PlayerResult> liste = gson.fromJson(json,
                new TypeToken<ArrayList<PlayerResult>>() {
                }.getType());
        return liste;
    }


    public String getUsername() {
        return username;
    }

    public String getWin() {
        return win;
    }

    public String getLose() {
        return lose;
    }
}
