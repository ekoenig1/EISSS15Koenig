package com.spiel21.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class RegistrationActivity extends ActionBarActivity {

    // Strings für den Reg-Bereich
    private String emailtext = "";
    private String passtext = "";
    EditText email;
    EditText pass1;
    EditText pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button create = (Button) findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    email = (EditText) findViewById(R.id.create_email);
                    pass1 = (EditText) findViewById(R.id.create_password1);
                    pass2 = (EditText) findViewById(R.id.create_password2);

                    if (pass1.getText().toString().equals(pass2.getText().toString())) {
                        passtext = pass1.getText().toString();
                        Toast.makeText(getApplication(), "Passwörter OK", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplication(), "Passwörter stimmen nicht überein", Toast.LENGTH_LONG).show();
                    }

                    emailtext = email.getText().toString();

                    String test = new AsyncTaskLogin(emailtext, passtext, "create").execute().get();

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

    // wird zurueck geleitet
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
