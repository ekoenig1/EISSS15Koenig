package com.spiel21.application.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Matches {

    // Konvertiert JSON in Klassen, http://json2csharp.com/
    private String _id;
    private String player1_id;
    private String player2_id;
    private String player3_id;
    private String player4_id;
    private String courts_id;
    private String date;
    private String time;

    private static Gson gson = new Gson();

    public Matches() {
    }

    public static Matches createMatch(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, Matches.class);
    }


    public static ArrayList<Matches> createMatchList(String json) {
        ArrayList<Matches> liste = gson.fromJson(json,
                new TypeToken<ArrayList<Matches>>() {
                }.getType());
        return liste;
    }

    public String get_id() {
        return _id;
    }

    public String getPlayer1_id() {
        return player1_id;
    }

    public String getPlayer2_id() {
        return player2_id;
    }

    public String getPlayer3_id() {
        return player3_id;
    }

    public String getPlayer4_id() {
        return player4_id;
    }

    public String getCourts_id() {
        return courts_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
