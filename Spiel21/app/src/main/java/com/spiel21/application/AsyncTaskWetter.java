package com.spiel21.application;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AsyncTaskWetter extends AsyncTask<String, Integer, String[]> {

    private final String LOG_TAG = AsyncTaskWetter.class.getSimpleName();

    private String[] leseXmlAsyncTaskWetterAus(String xmlString) {

        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xmlString));
            doc = db.parse(is);
        } catch (ParserConfigurationException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error: " + e.getMessage());
            return null;
        }

        Element xmlAsyncTaskWetter = doc.getDocumentElement();

        // Datum für jeden Tag auslesen
        NodeList elemente = xmlAsyncTaskWetter.getElementsByTagName("time");
        String[] AsyncTaskWetterArray = new String[elemente.getLength()];
        Node element;
        NamedNodeMap attribute;

        for (int i = 0; i < elemente.getLength(); i++) {
            element = elemente.item(i);
            attribute = element.getAttributes();
            String datum = attribute.getNamedItem("day").getNodeValue();
            AsyncTaskWetterArray[i] = datum;
        }


        // Temperaturwerte für jeden Tag auslesen
        elemente = xmlAsyncTaskWetter.getElementsByTagName("temperature");

        for (int i = 0; i < elemente.getLength(); i++) {
            element = elemente.item(i);
            attribute = element.getAttributes();
            String temperaturTag = attribute.getNamedItem("day").getNodeValue();
            AsyncTaskWetterArray[i] = AsyncTaskWetterArray[i] + "\n  Tag: " + temperaturTag + "°C";
            String temperaturNacht = attribute.getNamedItem("night").getNodeValue();
            AsyncTaskWetterArray[i] = AsyncTaskWetterArray[i] + " / Nacht: " + temperaturNacht + "°C";
            Log.v(LOG_TAG, "XML Output:" + AsyncTaskWetterArray[i]);
        }

        // Wetterzustand
        elemente = xmlAsyncTaskWetter.getElementsByTagName("symbol");

        for (int i = 0; i < elemente.getLength(); i++) {
            element = elemente.item(i);
            attribute = element.getAttributes();

            String wetterZustand = attribute.getNamedItem("name").getNodeValue();
            AsyncTaskWetterArray[i] = AsyncTaskWetterArray[i] + "\n (" + wetterZustand + ")";

            String wetterRegen = attribute.getNamedItem("number").getNodeValue();
            if (wetterZustand == "500") {
                //TODO: Warnung ausgeben
                //Toast.makeText(getActivity(), "REGEN!", Toast.LENGTH_SHORT).show();
            }
            //String zeitSonnenuntergang = attribute.getNamedItem("day").getNodeValue();
            //AsyncTaskWetterArray[i] = AsyncTaskWetterArray[i] + "  / wert2: " + zeitSonnenuntergang + "";
            //Log.v(LOG_TAG,"XML Output:" + zeitSonnenaufgang + ", " + zeitSonnenuntergang);
        }


        return AsyncTaskWetterArray;
    }


    @Override
    protected String[] doInBackground(String... strings) {

        if (strings.length == 0) { // Keine Eingangsparameter erhalten, Abbruch
            return null;
        }

        // konstruieren der Anfrage-URL fuer openweathermap.org
        // Parameter sind auf openweathermap.org/api beschrieben
        final String URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
        final String ORT = "q";
        final String DATENFORMAT = "mode";
        final String EINHEIT = "units";
        final String ANZAHL_TAGE = "cnt";
        final String SPRACHE = "lang";

        String ort = "Gummersbach";
        String datenformat = "json"; // json
        String einheit = "metric";
        int anzahlTage = 14;
        String sprache = "de";

        // baue den String zusammen fuer die URL
        String anfrageString = URL + ORT + "=" + ort;
        anfrageString += "&" + DATENFORMAT + "=" + datenformat;
        anfrageString += "&" + EINHEIT + "=" + einheit;
        anfrageString += "&" + ANZAHL_TAGE + "=" + anzahlTage;
        anfrageString += "&" + SPRACHE + "=" + sprache;


        // URL-Verbindung und der BufferedReader, werden im finally-Block geschlossen
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        // dieser String speichern wir die AsyncTaskWetter im XML-Format
        String AsyncTaskWetterXmlString = "";

        try {
            java.net.URL url = new URL(anfrageString);

            // Aufbau der Verbindung zu openweathermap.org
            httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();

            if (inputStream == null) { // Keine AsyncTaskWetter-Stream erhalten, Abbruch
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                AsyncTaskWetterXmlString += line + "\n";
            }
            if (AsyncTaskWetterXmlString.length() == 0) { // Keinen AsyncTaskWetter ausgelesen, Abbruch
                return null;
            }
            publishProgress(1, 1);

        } catch (IOException e) { // Beim Holen der Daten trat ein Fehler auf, Abbruch
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return leseXmlAsyncTaskWetterAus(AsyncTaskWetterXmlString);
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        // Einen Toast ausgeben, wenn doInBackground aufgerufen wird
        //Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String[] strings) {
        // Inhalt des ArrayAdapters wird geloescht und es werden neue Daten eingetragen
        if (strings != null) {
            //mWetterAdapter.clear();
            for (String dayForecastStr : strings) {
                //mWetterAdapter.add(dayForecastStr);
            }
        }
        // Hintergrundberechnungen sind jetzt beendet, info durch Toast
        //Toast.makeText(getActivity(), "AsyncTaskWetter geladen!", Toast.LENGTH_SHORT).show();
    }

}