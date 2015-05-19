package com.spiel21.application;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WetterActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Anzeigen des UP-Buttons in der Action-Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_wetter);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new WetterdetailFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wetter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar, as you specify a parent activity in AndroidManifest.xml
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    // Wetterdetail asl Fragment
    public static class WetterdetailFragment extends Fragment {
        public WetterdetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Fuellt die View mit dem Fragment
            View rootView = inflater.inflate(R.layout.fragment_wetter, container, false);

            // Die WetterdetailActivity wurde ueber einen Intent aufgerufen
            // man liest aus dem empfangenen Intent die uebermittelten Daten aus
            Intent empfangenerIntent = getActivity().getIntent();
            if (empfangenerIntent != null && empfangenerIntent.hasExtra(Intent.EXTRA_TEXT)) {
                String wetterinfo = empfangenerIntent.getStringExtra(Intent.EXTRA_TEXT);
                ((TextView) rootView.findViewById(R.id.wetter_text)).setText(wetterinfo);
            }

            return rootView;
        }
    }
}