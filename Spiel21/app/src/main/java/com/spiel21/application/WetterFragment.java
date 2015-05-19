package com.spiel21.application;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class WetterFragment extends Fragment {

    // ArrayAdapter als Membervariable
    ArrayAdapter<String> mWetterAdapter;

    public WetterFragment() {   }

    //--  damit Menueeintrag in der ActionBar ancklibar wird, muessen hier die 3 Methoden ueberschrieben werden
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Menue bekannt geben
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_wetterfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // pruefen, ob Menu ausgewaehlt worden ist anhand der ID
        int id = item.getItemId();

        // Daten aktuallisieren
        if (id == R.id.action_aktualisieren) {

            holeWetterDaten();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- ende --- //


    private void holeWetterDaten(){
        // erzeuge neue Instanz, starte anynchronen Task
        WetterDaten holeDatenTask = new WetterDaten();

        // Auslesen des ausgewaehlten Standorts aus den SharedPreferences
        SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String preferenceStandortKey = getString(R.string.preference_standort_key);
        String preferenceStandortDefault = getString(R.string.preference_standort_default);
        String standort = sPrefs.getString(preferenceStandortKey, preferenceStandortDefault);

        String preferenceKurzvorhersageKey = getString(R.string.preference_kurzvorhersage_key);
        Boolean kurzVorhersage = sPrefs.getBoolean(preferenceKurzvorhersageKey, false);

        String anzahlTage = "14";
        if (kurzVorhersage) anzahlTage = "7";
        holeDatenTask.execute(standort, anzahlTage);

        // Den Benutzer infromieren, das im Hintergrund Wetterdaten abgefragt wreden
        Toast.makeText(getActivity(), "Wetterdaten für " + standort + " werden abgefragt!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Beispieldaten
        // TODO: Soft ausfuehren
        String[] wetterArray = {
                "Bitte warten..."
        };

        // String-Array als ArrayList
        List<String> wochenVorhersage = new ArrayList<String>(Arrays.asList(wetterArray));

        // erstellen eines ArrayAdapters und hinzufuegen von Daten
        mWetterAdapter = new ArrayAdapter<String>(
                // aktuelle Umgebung der Activity, ID des Layouts, ID des TextViews, Daten aus dem Array
                getActivity(), R.layout.list_item_wetter, R.id.list_item_wetter_textview, wochenVorhersage);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // erstellen des ListViews, zuweisen des ArrayAdapters inkl Layouts
        ListView vorhersageListView = (ListView) rootView.findViewById(R.id.listview_wetter);
        vorhersageListView.setAdapter(mWetterAdapter);

        // registrieren eines OnItemClickListener fuer den ListView
        vorhersageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String wetterinfo = (String) adapterView.getItemAtPosition(position);

                // Intent erzeugen und Starten der WetterdetailActivity mit Intent
                Intent wetterdetailIntent = new Intent(getActivity(), WetterActivity.class);
                wetterdetailIntent.putExtra(Intent.EXTRA_TEXT, wetterinfo);
                startActivity(wetterdetailIntent);
                // Toast fuer den Benutzer
                Toast.makeText(getActivity(), wetterinfo, Toast.LENGTH_SHORT).show();
            }
        });

        holeWetterDaten();

        return rootView;
    }







    // Innnere Klasse fuert den asynchronen Task aus
    public class WetterDaten extends AsyncTask<String, Integer, String[]> {

        private final String LOG_TAG = WetterDaten.class.getSimpleName();

        private String[] leseXmlWetterdatenAus(String xmlString) {

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

            Element xmlWetterdaten = doc.getDocumentElement();

            // Datum für jeden Tag auslesen
            NodeList elemente = xmlWetterdaten.getElementsByTagName("time");
            String[] wetterdatenArray = new String[elemente.getLength()];
            Node element;
            NamedNodeMap attribute;

            for (int i = 0; i < elemente.getLength(); i++) {
                element = elemente.item(i);
                attribute = element.getAttributes();
                String datum = attribute.getNamedItem("day").getNodeValue();
                wetterdatenArray[i] = datum;
            }


            // Temperaturwerte für jeden Tag auslesen
            elemente = xmlWetterdaten.getElementsByTagName("temperature");

            for (int i = 0; i < elemente.getLength(); i++) {
                element = elemente.item(i);
                attribute = element.getAttributes();
                String temperaturTag = attribute.getNamedItem("day").getNodeValue();
                //int tempDay=Integer.parseInt(temperaturTag);
                wetterdatenArray[i] = wetterdatenArray[i] + "\n  Tag: " + temperaturTag + "°C";
                String temperaturNacht = attribute.getNamedItem("night").getNodeValue();
                wetterdatenArray[i] = wetterdatenArray[i] + " / Nacht: " + temperaturNacht + "°C";
                Log.v(LOG_TAG, "XML Output:" + wetterdatenArray[i]);
            }

            // Wetterzustand
            elemente = xmlWetterdaten.getElementsByTagName("symbol");

            for (int i = 0; i < elemente.getLength(); i++) {
                element = elemente.item(i);
                attribute = element.getAttributes();

                String wetterZustand = attribute.getNamedItem("name").getNodeValue();
                wetterdatenArray[i] = wetterdatenArray[i] + "\n (" + wetterZustand + ")";

                String wetterRegen = attribute.getNamedItem("number").getNodeValue();
                if (wetterZustand == "500") {
                    //TODO: Warnung ausgeben
                    Toast.makeText(getActivity(), "REGEN!", Toast.LENGTH_SHORT).show();
                }
                //String zeitSonnenuntergang = attribute.getNamedItem("day").getNodeValue();
                //wetterdatenArray[i] = wetterdatenArray[i] + "  / wert2: " + zeitSonnenuntergang + "";
                //Log.v(LOG_TAG,"XML Output:" + zeitSonnenaufgang + ", " + zeitSonnenuntergang);
            }


            return wetterdatenArray;
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
            String datenformat = "xml"; // json
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

            // dieser String speichern wir die Wetterdaten im XML-Format
            String wetterdatenXmlString = "";

            try {
                URL url = new URL(anfrageString);

                // Aufbau der Verbindung zu openweathermap.org
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                if (inputStream == null) { // Keine Wetterdaten-Stream erhalten, Abbruch
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    wetterdatenXmlString += line + "\n";
                }
                if (wetterdatenXmlString.length() == 0) { // Keinen Wetterdaten ausgelesen, Abbruch
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
            return leseXmlWetterdatenAus(wetterdatenXmlString);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            // Einen Toast ausgeben, wenn doInBackground aufgerufen wird
            Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            // Inhalt des ArrayAdapters wird geloescht und es werden neue Daten eingetragen
            if (strings != null) {
                mWetterAdapter.clear();
                for (String dayForecastStr : strings) {
                    mWetterAdapter.add(dayForecastStr);
                }
            }
            // Hintergrundberechnungen sind jetzt beendet, info durch Toast
            Toast.makeText(getActivity(), "Wetterdaten geladen!", Toast.LENGTH_SHORT).show();
        }

    }
}
