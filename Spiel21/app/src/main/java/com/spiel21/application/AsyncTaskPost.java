package com.spiel21.application;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AsyncTaskPost extends AsyncTask<String, Void, String> {

    // Strings für den Reg-Bereich
    private String gendertext = "";
    private String usernametext = "";
    private String birthtext = "";
    private String locationtext = "";
    private String passtext = "";
    private String phonetext = "";
    private String emailtext = "";
    private String _resource = "";

    public AsyncTaskPost(String gender, String username, String birth, String location,
                         String pass, String phone, String email, String resource) {
        gendertext = gender;
        usernametext = username;
        birthtext = birth;
        locationtext = location;
        passtext = pass;
        phonetext = phone;
        emailtext = email;
        _resource = resource;
    }

    Server server = new Server();

    // Serverdaten inkl Connection
    @Override
    protected String doInBackground(String... params) {
        HttpResponse anfrage;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(server.getAdresse() + _resource); // http://10.0.2.2:3000/

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("gender", gendertext);
            jsonObj.put("username", usernametext);
            jsonObj.put("birth", birthtext);
            jsonObj.put("location", locationtext);
            jsonObj.put("pass", passtext);
            jsonObj.put("phone", phonetext);
            jsonObj.put("email", emailtext);
            StringEntity parameter = new StringEntity(jsonObj.toString());
            post.setEntity(parameter);
            anfrage = client.execute(post);
            String responseText = EntityUtils.toString(anfrage.getEntity());

            return responseText;

            // Fehlerbehandlung
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "fail";
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return "fail";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        } catch (JSONException e) {
            e.printStackTrace();
            return "fail";
        }
    }


    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
