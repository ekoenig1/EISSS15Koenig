package com.spiel21.application.async;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AsyncTaskPost extends AsyncTask<String, Void, String> {

    // Strings fuer den POST-Bereich
    private JSONObject _jsonObject;
    private String _resource = "";

    public AsyncTaskPost(JSONObject jsonObj, String resource) {
        // baue ObjektID inkl Resource zusammen
        _jsonObject = jsonObj;
        _resource = resource;
    }

    //Server server = new Server();

    // Serverdaten inkl Verbindung zum Server
    @Override
    protected String doInBackground(String... params) {
        HttpResponse anfrage;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(Server.getAdresse() + _resource);

        try {
            StringEntity parameter = new StringEntity(_jsonObject.toString());
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
