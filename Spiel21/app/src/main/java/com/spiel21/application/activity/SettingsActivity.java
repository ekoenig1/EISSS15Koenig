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

        // Wetter-Einstellungen
        addPreferencesFromResource(R.xml.preferences);
        Preference standortPref = findPreference(getString(R.string.preference_standort_key));
        standortPref.setOnPreferenceChangeListener(this);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // gespeicherter Standort, wird genommen
        String saveStandort = sharedPrefs.getString(standortPref.getKey(), "");
        onPreferenceChange(standortPref, saveStandort);
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