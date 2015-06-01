package com.spiel21.application;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class User {

    // Konvertiert JSON in Klassen, http://json2csharp.com/
    private String _id;
    private String loss;
    private String gender;
    private String first;
    private String last;
    private String email;
    private String username;
    private String pass;
    private String phone;
    private String pic;
    private String birth;
    private String age;
    private String win;

    private static Gson gson = new Gson();

    public User() {
    }

    public static User createUser(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, User.class);
    }


    public static ArrayList<User> createUserList(String json) {
        ArrayList<User> liste = gson.fromJson(json,
                new TypeToken<ArrayList<User>>() {
                }.getType());
        return liste;
    }


    public String getId() {
        return _id;
    }

    public String getLoss() {
        return loss;
    }

    public String getGender() {
        return gender;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public String getPhone() {
        return phone;
    }

    public String getPic() {
        return pic;
    }

    public String getBirth() {
        return birth;
    }

    public String getAge() {
        return age;
    }

    public String getWin() {
        return win;
    }

}
