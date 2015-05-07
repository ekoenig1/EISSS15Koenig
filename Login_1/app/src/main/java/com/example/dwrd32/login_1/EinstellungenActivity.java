package com.example.dwrd32.login_1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class EinstellungenActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);


        // Toast.makeText(this, "Einstellungen-Activity gestartet.", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Zurück mit Back-Button.", Toast.LENGTH_SHORT).show();

        Preference standortPref = findPreference(getString(R.string.preference_standort_key));
        standortPref.setOnPreferenceChangeListener(this);


        // onPreferenceChange sofort aufrufen mit dem in SharedPreferences gespeicherten Standort
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String gespeicherterStandort = sharedPrefs.getString(standortPref.getKey(), "");
        onPreferenceChange(standortPref, gespeicherterStandort);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {

        preference.setSummary(value.toString());

        return true;
    }
}