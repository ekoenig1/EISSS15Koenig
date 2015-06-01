package com.spiel21.application;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class RegistrationActivity extends ActionBarActivity {

    // Strings für den Reg-Bereich
    private String gendertext = "";
    private String usernametext = "";
    private String birthtext = "";
    private String locationtext = "";
    private String passtext = "";
    private String phonetext = "";
    private String emailtext = "";

    EditText username;
    EditText birth;
    EditText location;
    EditText pass1;
    EditText pass2;
    EditText phone;
    EditText email;

    // drehen des Bildschirms verhindern
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Registrierung Bereich
        Button create = (Button) findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Is the button now checked?
                    boolean checkedMan = ((RadioButton) findViewById(R.id.radioButton_man)).isChecked();
                    boolean checkedWoman = ((RadioButton) findViewById(R.id.radioButton_women)).isChecked();
                    // Check which radio button was clicked
                    if (checkedMan&&!checkedWoman){
                        gendertext = "m";
                    }
                    else{
                        gendertext = "w";
                    }

                    username = (EditText) findViewById(R.id.create_username);
                    birth = (EditText) findViewById(R.id.create_birth);
                    location = (EditText) findViewById(R.id.create_location);
                    pass1 = (EditText) findViewById(R.id.create_password1);
                    pass2 = (EditText) findViewById(R.id.create_password2);
                    phone = (EditText) findViewById(R.id.create_phone);
                    email = (EditText) findViewById(R.id.create_email);


                    if (email.getText().toString().trim().equals("")){
                        Toast.makeText(getApplication(), "EMAIL FEHLT", Toast.LENGTH_LONG).show();
                    }


                    // die Passwoertfelder werden verglichen, duerfen nicht nicht leer sein
                    if (pass1.getText().toString().equals(pass2.getText().toString())) {
                        passtext = pass1.getText().toString().trim();
                        Toast.makeText(getApplication(), "Passwörter OK", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Passwörter stimmen nicht überein", Toast.LENGTH_LONG).show();
                    }

                    // Uebergabe
                    usernametext = username.getText().toString().trim();
                    birthtext = birth.getText().toString().trim();
                    locationtext = location.getText().toString().trim();
                    emailtext = email.getText().toString().trim();
                    phonetext = phone.getText().toString().trim();


                    String test = new AsyncTaskPost(gendertext, usernametext, birthtext, locationtext, passtext,
                            phonetext, emailtext, "/create").execute().get();

                    if (test.equals("OK")) {
                        Toast.makeText(getApplication(), "Benutzer angelegt", Toast.LENGTH_LONG).show();
                        changeActivity();
                    } else {
                        Toast.makeText(getApplication(), "Anlegen fehlgeschlagen", Toast.LENGTH_LONG).show();

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // wird zurueck geleitet zu der MainActivity
    private void changeActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
