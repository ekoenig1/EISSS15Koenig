package com.spiel21.application;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class KartenFragment extends Fragment implements GoogleMap.OnMapClickListener {


    private FragmentActivity myContext;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    LatLng START = new LatLng(50.67, 7.06); //50.6718884,7.06769,17 -> bonn




    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setOnMapClickListener(this);
        mMap.addMarker(new MarkerOptions()
                .position(START)
                .title("Marker"));
        //.draggable(true)
        //.snippet("Hier Basketballplatz markieren"));
        mMap.setMyLocationEnabled(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_karten, container, false);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Basketballplatz")
                .draggable(true));
        mMap.setMyLocationEnabled(true);
        //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
    }
}
