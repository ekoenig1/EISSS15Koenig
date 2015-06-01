package com.spiel21.application;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

/* Eduard König
 * EIS-Projekt, Spiel 21
 * SS 2015
 */


// Startbildschirm
public class MainActivity extends FragmentActivity {

    // Strings für den Loginbereich
    private String emailtext = "";
    private String passtext = "";
    EditText email;
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
                    email = (EditText) findViewById(R.id.email);
                    pass = (EditText) findViewById(R.id.pass);

                    emailtext = email.getText().toString();
                    passtext = pass.getText().toString();

                    String test = new AsyncTaskPost("", "", "", "", passtext, "", emailtext, "/login").execute().get();

                    if (test.equals("FAIL")) {
                        Toast.makeText(getApplication(), "Anmeldung fehlgeschlagen", Toast.LENGTH_LONG).show();

                    } else {

                        User benutzer = User.createUser(test);
                        Toast.makeText(getApplication(), benutzer.getEmail() + ", " + benutzer.getPass(), Toast.LENGTH_LONG).show();

                        //JSONObject obj = new JSONObject(test);
                        changeActivity();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        // Zugriff auf das Registirierungsformular
        TextView btnRegistration = (TextView) findViewById(R.id.textView_registrieren);
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

        // Direktzugriff auf die Karte *testweise*
        Button btnKarte = (Button) findViewById(R.id.button_karte);
        btnKarte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), KartenActivity.class));
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

}
