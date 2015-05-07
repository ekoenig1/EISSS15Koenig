package com.example.dwrd32.login_1;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CompassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompassFragment extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // die Sensoren ansprechen
    private TextView textViewCompass;
    private ImageView imageViewCompass;
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagnetometer;

    // Variablen fuer Sensordaten
    private float[] gravityData;
    private float[] geomagneticData;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CompassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompassFragment newInstance(String param1, String param2) {
        CompassFragment fragment = new CompassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;


    }

    public CompassFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compass, container, false);
        // das Textfeld initialisieren
        textViewCompass = (TextView) view.findViewById(R.id.text_compass);
        // eine ID fuer die Grafik vergeben
        imageViewCompass = (ImageView) view.findViewById(R.id.imageView_compass);

        // test
        Toast.makeText(getActivity(),"CompassFragment", Toast.LENGTH_LONG).show();

        // auf den Sensor-Service zugreifen
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        // die Sensoren initialisieren
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);



        return view;
    }


    @Override
    // Sensordaten untersuchen
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            gravityData = sensorEvent.values;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            geomagneticData = sensorEvent.values;
        // Die empfangene Daten kontrollieren
        if (gravityData != null && geomagneticData != null) {
            // die Sensordaten umwandeln, 3x3 Matrix
            float r[] = new float[9];
            float i[] = new float[9];
            // Rotationsdaten abfragen
            boolean success = SensorManager.getRotationMatrix(r, i, gravityData, geomagneticData);
            // die Rotationsdaten uebergeben
            if(success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(r, orientation);
                // Gradzahlen berechnen
                float compassAngle = orientation[0] * 180 / (float) Math.PI;
                // den Richungswinkel anzeigen
                textViewCompass.setText(Float.toString(compassAngle));
                // das Bild drehen
                imageViewCompass.setRotation(-compassAngle);
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    // Methoden fuer den Lebenszyklus, um Akku zu sparen //

    // die Sensoren einschalten
    @Override
    public void onStart() {
        super.onStart();
        // die Verzoegerung festlegen
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    public void onResume() {
        super.onResume();
        // die Verzoegerung festlegen
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorMagnetometer, SensorManager.SENSOR_DELAY_UI);
    }

    // die Sensoren ausschalten
    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        sensorManager.unregisterListener(this);
    }
    // -- ende -- //

}
