package com.spiel21.application;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncTaskGet extends AsyncTask<String, Integer, String> {

    protected InputStream is = null;
    protected String result;

    @Override
    protected String doInBackground(String... params) {
        try {
            // Verbindung wird zum Server aufberaut und gespeichert was von Server geliefert wird.
            URL urlVomServer = new URL(params[0]);
            HttpURLConnection urlVerbindung = (HttpURLConnection) urlVomServer.openConnection();
            urlVerbindung.setRequestMethod("GET");
            is = new BufferedInputStream(urlVerbindung.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert InputStream to byte array
        // http://stackoverflow.com/questions/1264709/convert-inputstream-to-byte-array-in-java
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[(int) is.toString().length()];
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            result = new String(buffer.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(InputStream is) {
    }
}
