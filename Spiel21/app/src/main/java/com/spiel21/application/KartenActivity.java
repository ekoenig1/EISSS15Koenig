package com.spiel21.application;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class KartenActivity extends FragmentActivity
        implements LocationListener, GoogleMap.OnMapClickListener {

    // Variablen fuer GoogleMaps
    private GoogleMap mMap; // koennte null sein, wenn Google Play services nicht verfuegbar
    LatLng START = new LatLng(51.0296774, 7.5744226); //50.6718884,7.06769,17 -> ~Bonn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_karten);


        // versuche hier, die Karte von der SupportMapFragment zu erhalten
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        // setzte den Kartentyp
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // zeige die aktuelle position an
        mMap.setMyLocationEnabled(true);


        // wartet auf den ClickListner, Marker setzen
        mMap.setOnMapClickListener(this);

        // Voreinstellungen fuer den Start
        setUpMap();


        // ---- aktuelle Position finden --- //
        // http://blog.teamtreehouse.com/beginners-guide-location-android
        // TODO: anzeigen lassen
        LocationManager locationManager;
        // Get the LocationManager object from the System Service LOCATION_SERVICE
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Create a criteria object needed to retrieve the provider
        Criteria criteria = new Criteria();
        // Get the name of the best available provider
        String provider = locationManager.getBestProvider(criteria, true);
        // We can use the provider immediately to get the last known location
        Location location = locationManager.getLastKnownLocation(provider);
        // request that the provider send this activity GPS updates every 20 seconds
        locationManager.requestLocationUpdates(provider, 20000, 0, this);

    }

    // Menue rechts, muss spater druch Drawer erstetzt werden
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // hier kann ein Standart Marker gesetzt werden
    private void setUpMap() {

        // bewege die Kamera zur Start Position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(START));
        // Zoom fuer die Kamera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));

        // hier wird ein Marker gesetzt
        mMap.addMarker(new MarkerOptions().position(START).title("Marker").snippet("Testmarker"));
    }


    // der Marker wird gesetzt, wenn der Bunutzer die Karte beruehrt, voher implements GoogleMap.OnMapClickListener
    @Override
    public void onMapClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("Basketballplatz").draggable(true));
        mMap.setMyLocationEnabled(true);  //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        // Auskunft fuer den Benutzer, wohin wurde geklickt
        Toast.makeText(getApplication(), "Position: " + latLng, Toast.LENGTH_LONG).show();
        // bewege die Kamera zur geklickten Position
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
