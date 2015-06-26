package com.spiel21.application.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Result {

    private String _id;
    private String lose;
    private String player_id;
    private String match_id;
    private String win;

    private static Gson gson = new Gson();

    public Result() {
    }

    public static Result createResult(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, Result.class);
    }


    public static ArrayList<Result> createResultList(String json) {
        ArrayList<Result> liste = gson.fromJson(json,
                new TypeToken<ArrayList<Result>>() {
                }.getType());
        return liste;
    }


    public String getId() {
        return _id;
    }

    public String getLose() {
        return lose;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public String getMatch_id() {
        return match_id;
    }

    public String getWin() {
        return win;
    }
}
