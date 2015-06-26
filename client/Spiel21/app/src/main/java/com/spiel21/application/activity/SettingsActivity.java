package com.spiel21.application.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.spiel21.application.R;

public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    // drehen des Bildschirms verhindern
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Einstellungen
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Wetter-Einstellungen (Standort)
        Preference standortPref = findPreference(getString(R.string.preference_standort_key));
        standortPref.setOnPreferenceChangeListener(this);
        String saveStandort = sharedPrefs.getString(standortPref.getKey(), "");
        onPreferenceChange(standortPref, saveStandort);

        // Benutzer-Einstellungen (Benutzer, Passwort)
        Preference benutzerPref = findPreference(getString(R.string.preference_benutzername_key));
        benutzerPref.setOnPreferenceChangeListener(this);
        String saveBenutzer = sharedPrefs.getString(benutzerPref.getKey(), "");
        onPreferenceChange(benutzerPref, saveBenutzer);

        Preference passwortPref = findPreference(getString(R.string.preference_passwort_key));
        passwortPref.setOnPreferenceChangeListener(this);
        String savePasswort = sharedPrefs.getString(passwortPref.getKey(), "");
        onPreferenceChange(passwortPref, savePasswort);

        // Info fuer den Benutzer
        Toast.makeText(this, "Zurück mit Back-Button.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        // hier ist die Ausgabe
        preference.setSummary(value.toString());
        return true;
    }
}