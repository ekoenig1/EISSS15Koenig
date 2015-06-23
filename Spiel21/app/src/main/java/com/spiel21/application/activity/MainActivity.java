package com.spiel21.application.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.spiel21.application.R;
import com.spiel21.application.async.AsyncTaskPost;
import com.spiel21.application.util.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/****************************
 *  Eduard König
 * EIS-Projekt, Spiel 21
 * SS 2015
 **************************/


// Startbildschirm
public class MainActivity extends FragmentActivity
        implements Preference.OnPreferenceChangeListener {

    // Strings für den Loginbereich
    private String usernametext = "";
    private String passtext = "";
    EditText username;
    EditText pass;

    // drehen des Bildschirms verhindern
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Anmelden Bereich
        Button btnAnmelden = (Button) findViewById(R.id.button_anmelden);
        btnAnmelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    username = (EditText) findViewById(R.id.editText_username);
                    pass = (EditText) findViewById(R.id.editText_pass);

                    usernametext = username.getText().toString();
                    passtext = pass.getText().toString();

                    JSONObject jsonObj = new JSONObject();
                    try {
                        jsonObj.put("username", usernametext);
                        jsonObj.put("pass", passtext);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String stringLogin = new AsyncTaskPost(jsonObj, "/users/login").execute().get();

                    // Benutzer wird informiert bei der falschen Eingabe
                    if (stringLogin.equals("FAIL")) {
                        Toast.makeText(getApplication(), "Anmeldung fehlgeschlagen", Toast.LENGTH_LONG).show();

                    } else {
                        // Testausgabe mit Username und Passwort
                        Users benutzer = Users.createUser(stringLogin);
                        Toast.makeText(getApplication(), benutzer.getUsername() + ", " + benutzer.getPass(), Toast.LENGTH_LONG).show();

                        // TODO: Hier den Benutzer und Passwort speichern

                        // anschliesend wird zur Navigation gewechselt
                        changeActivity();
                    }
                    // Fehlerbehandlung
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        // Zugriff auf das Registirierungsformular
        TextView btnRegistration = (TextView) findViewById(R.id.button_registrieren);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToRegistration();
            }
        });

        // Direktzugriff auf die NavigationActivity *testweise*
        Button btnMenu = (Button) findViewById(R.id.button_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity();
            }
        });

    }


    // hier kann sich der Benutzer in der Klasse registrieren
    private void changeToRegistration() {
        startActivity(new Intent(this, RegistrationActivity.class));
    }

    // nach dem Login wird das Menue aufgerufen
    private void changeActivity() {
        startActivity(new Intent(this, NavigationActivity.class));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }
}
