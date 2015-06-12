package com.spiel21.application.activity;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
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
import com.spiel21.application.async.Server;
import com.spiel21.application.util.Courts;
import com.spiel21.application.util.TimeDialog;
import com.spiel21.application.util.Users;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class ChallengeAktivity extends ActionBarActivity {

    private String usernametext = "";
    private String passtext = "";


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
        EditText editDatum = (EditText) findViewById(R.id.editText_date);
        editDatum.setFocusable(false);
        editDatum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DateDialog2();
                newFragment.show(getSupportFragmentManager(), "DatePicker");

            }
        });

        // Listener fuers Zeit
        EditText editZeit = (EditText) findViewById(R.id.editText_time);
        editZeit.setFocusable(false);
        editZeit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimeDialog(R.id.editText_time);
                newFragment.show(getSupportFragmentManager(), "TimePicker");
            }
        });

        // Basketballplaetze
        ArrayList<Courts> courtsArrayList = null;
        try {
            String result = new AsyncTaskGet().execute(new Server().getAdresse() + "/courts/").get();
            courtsArrayList = Courts.createCourtList(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        String[] courts = new String[courtsArrayList.size()];
        for (int i = 0; i < courtsArrayList.size(); i++) {
            courts[i] = courtsArrayList.get(i).getName();
        }
        ArrayList<String> listeCourts = new ArrayList<String>(Arrays.asList(courts));
        Spinner spinner = (Spinner) findViewById(R.id.spinner_courts);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_items, R.id.list_items_textview, listeCourts);
        adapter.setDropDownViewResource(R.layout.list_items);
        spinner.setAdapter(adapter);

        // Spieler aus der Collection auslesen
        ArrayList<Users> usersArrayList = null;
        try {
            String result = new AsyncTaskGet().execute(new Server().getAdresse() + "/users/").get();
            usersArrayList = Users.createUserList(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // TODO: Liste sollte am Anfang leer sein
        // String erstellen
        String[] users = new String[usersArrayList.size()];
        for (int i = 0; i < usersArrayList.size(); i++) {
            //
            users[i] = usersArrayList.get(i).getUsername();
        }
        ArrayList<String> listeUsers = new ArrayList<String>(Arrays.asList(users));
        // spieler 1
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner_user1);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                R.layout.list_items, R.id.list_items_textview, listeUsers);
        // spieler 2
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner_user2);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                R.layout.list_items, R.id.list_items_textview, listeUsers);
        // spieler 3
        Spinner spinner3 = (Spinner) findViewById(R.id.spinner_user3);
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                R.layout.list_items, R.id.list_items_textview, listeUsers);


        // Items (Spinner) werden befuellt
        adapter1.setDropDownViewResource(R.layout.list_items);
        adapter2.setDropDownViewResource(R.layout.list_items);
        adapter3.setDropDownViewResource(R.layout.list_items);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);


        //String stringLogin = new AsyncTaskPost("", usernametext, "", "", passtext, "", "", "/login").execute().get();

        //Users benutzer = Users.createUser(stringLogin);
        //Toast.makeText(getApplication(), benutzer.getUsername() + ", " + benutzer.getPass(), Toast.LENGTH_LONG).show();

        // Herausfordern Button
        Button btnMenu = (Button) findViewById(R.id.button_herausfordern);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Basketballplatz: " + adapter
                        + "Spieler 1: " + adapter1.getContext().toString() // Wie bekommt man hier den Inhalt?
                        + "Spieler 2: " + adapter2
                        + "Spieler 3: " + adapter3, Toast.LENGTH_LONG).show();

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
            populateSetDate(year, month + 1, day);
        }

        // auswahl des Datums im EditText
        public void populateSetDate(int jahr, int monat, int tag) {
            EditText editTextDate = (EditText) getActivity().findViewById(R.id.editText_date);
            editTextDate.setText(tag + "." + monat + "." + jahr);

            //HIER

        }
    }
}
