package com.example.dwrd32.login_1;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    private String emailtext = "";
    private String passtext = "";
    EditText email;
    EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
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
                        //TODO: hier Funktion fuer andere activity
                        changeActivity();
                    }
                    else
                    {
                        Toast.makeText(getApplication(), "Anmeldung fehlgeschlagen", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        Button btnMenu = (Button) findViewById(R.id.button_menu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplication(),"Menu wurde gedrueckt",Toast.LENGTH_LONG).show();
                changeActivity();
            }
        });

        Button create = (Button) findViewById(R.id.button_create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    email = (EditText) findViewById(R.id.email_create);
                    pass = (EditText) findViewById(R.id.pass_create);

                    emailtext = email.getText().toString();
                    passtext = pass.getText().toString();
                    String test = new AsyncTaskLogin(emailtext, passtext, "create").execute().get();

                    if (test.equals("OK")) {
                        Toast.makeText(getApplication(), "Benutzer angelegt", Toast.LENGTH_LONG).show();
                        //TODO: hier Funktion fuer andere activity
                        changeActivity();
                    }
                    else
                    {
                        Toast.makeText(getApplication(), "Create fehlgeschlagen", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    // nach dem Login wird die Klasse aufgerufen
    private void changeActivity(){
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        // TODO: hier fragen, NavigationActivity.newInstance("a","b");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Hier die Aktionsleiste mit dem Menue
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
