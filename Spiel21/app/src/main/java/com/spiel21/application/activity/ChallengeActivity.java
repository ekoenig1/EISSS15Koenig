package com.spiel21.application.activity;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.spiel21.application.R;
import com.spiel21.application.async.AsyncTaskGet;
import com.spiel21.application.async.AsyncTaskPost;
import com.spiel21.application.async.Server;
import com.spiel21.application.util.Courts;
import com.spiel21.application.util.TimeDialog;
import com.spiel21.application.util.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ChallengeActivity extends ActionBarActivity {

    private String usernametext = "";
    private String passtext = "";
    private String player1_id_text = "";
    private String player2_id_text = "";
    private String player3_id_text = "";
    private String player4_id_text = "";
    private String courts_id_text = "";
    public HashMap<String, String> hash = new HashMap<>();
    private static final String LOG_TAG = "MyActivity";


    // drehen des Bildschirms verhindern
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        // Listener fuers Datum
        final EditText editDatum = (EditText) findViewById(R.id.editText_date);
        editDatum.setFocusable(false);
        editDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DateDialog2();
                newFragment.show(getSupportFragmentManager(), "DatePicker");

            }
        });

        // Listener fuers Zeit
        final EditText editZeit = (EditText) findViewById(R.id.editText_time);
        editZeit.setFocusable(false);
        editZeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimeDialog(R.id.editText_time);
                newFragment.show(getSupportFragmentManager(), "TimePicker");
            }
        });

        // Basketballplaetze aus der Collection auslesen --->
        ArrayList<Courts> courtsArrayList = null;
        try {
            String result = new AsyncTaskGet().execute(Server.getAdresse() + "/courts/").get();
            courtsArrayList = Courts.createCourtList(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        final String[] courts = new String[courtsArrayList.size()];
        for (int i = 0; i < courtsArrayList.size(); i++) {
            courts[i] = courtsArrayList.get(i).getName();
            //hashmap
            String key = courtsArrayList.get(i).getName();
            String value = courtsArrayList.get(i).getId();
            hash.put(key, value);
        }
        ArrayList<String> listeCourts = new ArrayList<String>(Arrays.asList(courts));
        final Spinner spinner_courts = (Spinner) findViewById(R.id.spinner_courts);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_items_weather, R.id.list_items_textview, listeCourts);
        adapter.setDropDownViewResource(R.layout.list_items_weather);
        spinner_courts.setAdapter(adapter);

        // -- auslesen ende ---<


        // Spieler aus der Collection auslesen --->
        ArrayList<Users> usersArrayList = null;
        try {
            String result = new AsyncTaskGet().execute(Server.getAdresse() + "/users/").get();
            usersArrayList = Users.createUserList(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // String erstellen
        String[] users = new String[usersArrayList.size()];
        for (int i = 0; i < usersArrayList.size(); i++) {
            users[i] = usersArrayList.get(i).getUsername();
            //hashmap
            String key = usersArrayList.get(i).getUsername();
            String value = usersArrayList.get(i).getId();
            hash.put(key, value);
        }
        ArrayList<String> listeUsers = new ArrayList<String>(Arrays.asList(users));
        // Liste ist leer am Anfang
        listeUsers.add(0, "");

        // spieler 1
        final Spinner spinner_player1 = (Spinner) findViewById(R.id.spinner_user1);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.list_items_weather, R.id.list_items_textview, listeUsers);
        // spieler 2
        final Spinner spinner_player2 = (Spinner) findViewById(R.id.spinner_user2);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.list_items_weather, R.id.list_items_textview, listeUsers);
        // spieler 3
        final Spinner spinner_player3 = (Spinner) findViewById(R.id.spinner_user3);
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                R.layout.list_items_weather, R.id.list_items_textview, listeUsers);


        // Items (Spinner) werden befuellt
        adapter1.setDropDownViewResource(R.layout.list_items_weather);
        adapter2.setDropDownViewResource(R.layout.list_items_weather);
        adapter3.setDropDownViewResource(R.layout.list_items_weather);
        spinner_player1.setAdapter(adapter1);
        spinner_player2.setAdapter(adapter2);
        spinner_player3.setAdapter(adapter3);

        // -- auslesen ende ---<


        // Herausfordern Button
        Button btnMenu = (Button) findViewById(R.id.button_herausfordern);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Testweise Info
                Toast.makeText(getApplication(), "Basketballplatz: " + spinner_courts.getSelectedItem().toString()
                        + "Spieler 1: " + spinner_player1.getSelectedItem().toString()
                        + "Spieler 2: " + spinner_player2.getSelectedItem().toString()
                        + "Spieler 3: " + spinner_player3.getSelectedItem().toString(), Toast.LENGTH_LONG).show();

                JSONObject jsonObj = new JSONObject();
                try {
                    // TODO: Player 1 speichern und hier ausgeben
                    // Player 1 ist LOGIN-Benutzer
                    jsonObj.put("player1_id", "5576ead315620cf10cd07318"); // Test-User, ObjectId("5576ead315620cf10cd07318")
                    jsonObj.put("player2_id", hash.get(spinner_player1.getSelectedItem().toString()));
                    jsonObj.put("player3_id", hash.get(spinner_player2.getSelectedItem().toString()));
                    jsonObj.put("player4_id", hash.get(spinner_player3.getSelectedItem().toString()));
                    jsonObj.put("courts_id", hash.get(spinner_courts.getSelectedItem().toString()));

                    /*TimeZone zone = TimeZone.getTimeZone("GMT");
                    DateFormat format = new SimpleDateFormat("YYYY-MM-DD");
                    format.setTimeZone(zone);
                    String t = format.format(new Date());*/

                    jsonObj.put("date", editDatum.getText().toString());
                    jsonObj.put("time", editZeit.getText().toString());

                     // Test-Logs
                    Log.e(LOG_TAG, "Status: " + hash.get(spinner_courts.getSelectedItem().toString()));
                    Log.e(LOG_TAG, "Status: " + hash.get(spinner_player1.getSelectedItem().toString()));
                    Log.e(LOG_TAG, "Status: " + hash.get(spinner_player2.getSelectedItem().toString()));
                    Log.e(LOG_TAG, "Status: " + hash.get(spinner_player3.getSelectedItem().toString()));
                    Log.e(LOG_TAG, "Status: " + editDatum.getText().toString());



                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error: " + e.getMessage());
                    e.printStackTrace();
                }

                String stringMatch = null;
                try {
                    stringMatch = new AsyncTaskPost(jsonObj, "/matches").execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


                if (stringMatch.equals("OK")) {
                    Toast.makeText(getApplication(), "Match angelegt", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplication(), "Match fehlgeschlagen", Toast.LENGTH_LONG).show();

                }

                // Fertig, springe zurueck zur letzten Activity
                finish();
            }
        });
    }


    // Menue rechts, muss spaeter druch Drawer erstetzt werden
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("ValidFragment")
    public class DateDialog2 extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // erstellt eine neue Instanz des DatePickerDialog und senden es
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            /*
            month = DatePicker.getMonth()+1;
            day = DatePicker.getDayOfMonth();

            request.addProperty("datetext",datePicker.getYear()+"-"
            +((month.toString().length()   == 1 ? "0"+month.toString():month.toString()) )+"-"
            +((day.toString().length() == 1 ? "0"+day.toString():day.toString())));
            */

            populateSetDate(year, month + 1, day);
        }

        // auswahl des Datums im EditText
        public void populateSetDate(int jahr, int monat, int tag) {
            EditText editTextDate = (EditText) getActivity().findViewById(R.id.editText_date);

            editTextDate.setText(tag + "." + monat + "." + jahr);

        }
    }
}
