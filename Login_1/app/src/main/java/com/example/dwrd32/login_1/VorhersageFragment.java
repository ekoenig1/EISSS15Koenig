package com.example.dwrd32.login_1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

public class VorhersageFragment extends Fragment {

    // Der ArrayAdapter ist jetzt eine Membervariable der Klasse VorhersageFragment
    ArrayAdapter<String> mVorhersageAdapter;

    public VorhersageFragment() {    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Menü bekannt geben, dadurch kann unser Fragment Menü-Events verarbeiten
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vorhersagefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Prüfen, ob Menü-Elemente der Action Bar ausgewählt wurden
        // Wir prüfen, ob Menü-Element mit der ID "action_aktualisieren" ausgewählt wurde
        // Wurde unser Button gedrückt, holen wir Wetterdaten und geben eine Meldung aus
        int id = item.getItemId();
        if (id == R.id.action_aktualisieren) {

            // Erzeugen einer Instanz von HoleWetterdatenTask und starten des asynchronen Tasks
            HoleWetterdatenTask holeDatenTask = new HoleWetterdatenTask();

            // Auslesen des ausgewählten Standorts aus den SharedPreferences
            SharedPreferences sPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String preferenceStandortKey = getString(R.string.preference_standort_key);
            String preferenceStandortDefault = getString(R.string.preference_standort_default);
            String standort = sPrefs.getString(preferenceStandortKey,preferenceStandortDefault);

            String preferenceKurzvorhersageKey = getString(R.string.preference_kurzvorhersage_key);
            Boolean kurzVorhersage = sPrefs.getBoolean(preferenceKurzvorhersageKey, false);

            String anzahlTage = "15";
            if (kurzVorhersage) anzahlTage = "7";

            holeDatenTask.execute(standort, anzahlTage);

            // Den Benutzer informieren, dass neue Wetterdaten im Hintergrund abgefragt werden
            Toast.makeText(getActivity(), "Wetterdaten für " + standort + " werden abgefragt!", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String [] vorhersageArray = {
                "Montag - Regen - 14 °C",
                "Dienstag - Regen - 12 °C",
                "Mittwoch - Sonne - 17 °C",
                "Donnerstag - Regen - 8 °C",
                "Freitag - Wolken - 3 °C",
                "Samstag - Schnee -2 °C",
                "Sonntag - Sonne - 1 °C",
        };

        List<String> wochenVorhersage = new ArrayList<String>(Arrays.asList(vorhersageArray));

        mVorhersageAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // Die aktuelle Umgebung (diese Activity)
                        R.layout.list_item_vorhersage, // ID der XML-Layout Datei
                        R.id.list_item_vorhersage_textview, // ID des TextViews
                        wochenVorhersage); // Beispieldaten in einer ArrayList

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Eine Referenz zu unserem ListView, und Verbinden des ArrayAdapters mit dem ListView
        // Anschließend registrieren eines OnItemClickListener für den ListView
        ListView vorhersageListView = (ListView) rootView.findViewById(R.id.listview_vorhersage);
        vorhersageListView.setAdapter(mVorhersageAdapter);
        vorhersageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String wetterinfo = (String) adapterView.getItemAtPosition(position);

                // Intent erzeugen und Starten der WetterdetailActivity mit Intent
                Intent wetterdetailIntent = new Intent(getActivity(), WetterdetailActivity.class);
                wetterdetailIntent.putExtra(Intent.EXTRA_TEXT, wetterinfo);
                startActivity(wetterdetailIntent);
            }
        });

        String LOG_TAG = VorhersageFragment.class.getSimpleName();

        Log.v(LOG_TAG, "verbose     - Meldung");
        Log.d(LOG_TAG, "debug       - Meldung");
        Log.i(LOG_TAG, "information - Meldung");
        Log.w(LOG_TAG, "warning     - Meldung");
        Log.e(LOG_TAG, "error       - Meldung");

        return rootView;
    }

    // Innere Klasse HoleWetterdatenTask führt den asynchronen Task auf eigenem Arbeitsthread aus
    public class HoleWetterdatenTask extends AsyncTask<String, Integer, String[]> {

        private final String LOG_TAG = HoleWetterdatenTask.class.getSimpleName();


        private String[] leseXmlWetterdatenAus(String xmlString) {

            Document doc;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));
                doc = db.parse(is);
            } catch (ParserConfigurationException e) {
                Log.e(LOG_TAG,"Error: " + e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e(LOG_TAG,"Error: " + e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e(LOG_TAG,"Error: " + e.getMessage());
                return null;
            }

            Element xmlWetterdaten = doc.getDocumentElement();

            // Datum für jeden Tag auslesen
            NodeList elemente = xmlWetterdaten.getElementsByTagName("time");
            String[] wetterdatenArray = new String[elemente.getLength()];
            Node element;
            NamedNodeMap attribute;

            for( int i=0; i<elemente.getLength(); i++ ) {
                element = elemente.item(i);
                attribute = element.getAttributes();
                String datum = attribute.getNamedItem("day").getNodeValue();
                wetterdatenArray[i] = datum;
            }

            // Temperaturwerte für jeden Tag auslesen
            elemente = xmlWetterdaten.getElementsByTagName("temperature");
            for( int i=0; i<elemente.getLength(); i++ ) {
                element = elemente.item(i);
                attribute = element.getAttributes();
                String temperaturTag = attribute.getNamedItem("day").getNodeValue();
                wetterdatenArray[i] = wetterdatenArray[i] + "   Tag: " + temperaturTag + "°C";
                String temperaturNacht = attribute.getNamedItem("night").getNodeValue();
                wetterdatenArray[i] = wetterdatenArray[i] + " / Nacht: "  + temperaturNacht + "°C";

                Log.v(LOG_TAG,"XML Output:" + wetterdatenArray[i]);
            }

            // Sonnenauf- und Sonnenuntergang auslesen
            //elemente = xmlWetterdaten.getElementsByTagName("sun");
            //attribute = entries.item(0).getAttributes();
            //String zeitSonnenaufgang = attribute.getNamedItem("rise").getNodeValue();
            //String zeitSonnenuntergang = attribute.getNamedItem("set").getNodeValue();
            //Log.v(LOG_TAG,"XML Output:" + zeitSonnenaufgang + ", " + zeitSonnenuntergang);

            return wetterdatenArray;
        }

        @Override
        protected String[] doInBackground(String... strings) {

            if (strings.length == 0) { // Keine Eingangsparameter erhalten, Abbruch
                return null;
            }

            // Wir konstruieren die Anfrage-URL für openweathermap.org
            // Parameter sind auf openweathermap.org/api beschrieben
            final String URL_PARAMETER = "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String ORT_PARAMETER = "q";
            final String DATENFORMAT_PARAMETER = "mode";
            final String EINHEIT_PARAMETER = "units";
            final String ANZAHL_TAGE_PARAMETER = "cnt";
            final String SPRACHE_PARAMETER = "lang";

            String ort = strings[0];
            String datenformat = "xml";
            String einheit = "metric";
            int anzahlTage = Integer.parseInt(strings[1]);
            String sprache = "de";

            String anfrageString = URL_PARAMETER + ORT_PARAMETER + "=" + ort;
            anfrageString += "&" + DATENFORMAT_PARAMETER + "=" + datenformat;
            anfrageString += "&" + EINHEIT_PARAMETER + "=" + einheit;
            anfrageString += "&" + ANZAHL_TAGE_PARAMETER + "=" + anzahlTage;
            anfrageString += "&" + SPRACHE_PARAMETER + "=" + sprache;

            Log.v(LOG_TAG, "Zusammengesetzter Anfrage-String: " + anfrageString);

            // Die URL-Verbindung und der BufferedReader, werden im finally-Block geschlossen
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedReader = null;

            // In diesen String speichern wir die Wetterdaten im XML-Format
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
                //Log.v(LOG_TAG, "Wetterdaten XML-String: " + wetterdatenXmlString);
                publishProgress(1,1);

            } catch (IOException e) { // Beim Holen der Daten trat ein Fehler auf, Abbruch
                Log.e(LOG_TAG, "Error ", e);
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

            // Auf dem Bildschirm geben wir eine Statusmeldung aus, immer wenn
            // publishProgress(int...) in doInBackground(String...) aufgerufen wird
            Toast.makeText(getActivity(), values[0] + " von " + values[1] + " geladen", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(String[] strings) {

            // Wir löschen den Inhalt des ArrayAdapters und fügen den neuen Inhalt ein
            // Der neue Inhalt ist der Rückgabewert von doInBackground(String...) also
            // der StringArray gefüllt mit Online-Wetterdaten
            if (strings != null) {
                mVorhersageAdapter.clear();
                for (String dayForecastStr : strings) {
                    mVorhersageAdapter.add(dayForecastStr);
                }
            }

            // Hintergrundberechnungen sind jetzt beendet, darüber informieren wir den Benutzer
            Toast.makeText(getActivity(), "Wetterdaten vollständig geladen!", Toast.LENGTH_SHORT).show();
        }
    }
}
