package com.spiel21.application;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AsyncTaskLogin extends AsyncTask<String, Void, String> {

    private String emailtext = "";
    private String passtext = "";
    private String _resource = "";

    public AsyncTaskLogin(String email, String pass, String resource) {
        emailtext = email;
        passtext = pass;
        _resource = resource;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpResponse response;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://10.0.2.2:3000/" + _resource);
        HttpGet get = new HttpGet();

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email", emailtext);
            jsonObj.put("pass", passtext);
            StringEntity para = new StringEntity(jsonObj.toString());
            post.setEntity(para);

            //JSONObject json = new JSONObject(responseText);

            //request.setURI(new URI("http://10.0.2.2:3000/login"));
            response = client.execute(post);
            //response = client.execute(get);

            String responseText = EntityUtils.toString(response.getEntity());
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
