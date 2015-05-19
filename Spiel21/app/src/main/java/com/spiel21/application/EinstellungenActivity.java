package com.spiel21.application;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class EinstellungenActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference standortPref = findPreference(getString(R.string.preference_standort_key));
        standortPref.setOnPreferenceChangeListener(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // gespeicherter Standort
        String saveStandort = sharedPrefs.getString(standortPref.getKey(), "");
        onPreferenceChange(standortPref, saveStandort);


        Toast.makeText(this, "Einstellungen-Activity gestartet.", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Zurück mit Back-Button.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        // hier ist die Ausgabe...
        preference.setSummary(value.toString());
        return true;
    }
}