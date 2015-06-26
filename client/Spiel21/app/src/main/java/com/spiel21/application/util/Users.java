package com.spiel21.application.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class Users {

    // Konvertiert JSON in Klassen, http://json2csharp.com/
    private String _id;
    private String age;
    private String gender;
    private String email;
    private String username;
    private String city;
    private String pass;
    private String phone;
    private String pic;
    private String birth;

    private static Gson gson = new Gson();

    public Users() {
    }

    public static Users createUser(String json) {
        Gson gson = new Gson();
        json = json.replace('[', ' ');
        json = json.replace(']', ' ');
        return gson.fromJson(json, Users.class);
    }


    public static ArrayList<Users> createUserList(String json) {
        ArrayList<Users> liste = gson.fromJson(json,
                new TypeToken<ArrayList<Users>>() {
                }.getType());
        return liste;
    }


    public String getId() {
        return _id;
    }


    public String getGender() {
        return gender;
    }

    public String getCity() {
        return city;
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

}
