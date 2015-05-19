package com.spiel21.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
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
public class MainActivity extends ActionBarActivity {

    // Strings für den Loginbereich
    private String emailtext = "";
    private String passtext = "";
    EditText email;
    EditText pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Anmelden Bereich
        Button btnAnmelden = (Button) findViewById(R.id.button_anmelden);
        btnAnmelden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(),"hallo",Toast.LENGTH_LONG).show();
                try {
                    email = (EditText) findViewById(R.id.email);
                    pass = (EditText) findViewById(R.id.pass);

                    emailtext = email.getText().toString();
                    passtext = pass.getText().toString();
                    String test = new AsyncTaskLogin(emailtext, passtext, "login").execute().get();

                    if (test.equals("OK")) {
                        Toast.makeText(getApplication(), test, Toast.LENGTH_LONG).show();
                        changeActivity();
                    } else {
                        Toast.makeText(getApplication(), "Anmeldung fehlgeschlagen", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        //
        TextView btnRegistration = (TextView) findViewById(R.id.textView_registrieren);
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToRegistration();
            }
        });

        // Direktzugriff auf die NavigationActivity
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
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    // nach dem Login wird die NavigationActivity aufgerufen
    private void changeActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }


    /*
    // Menue rechts
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
