package com.example.bluefacetest1;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;


import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity<LocationServices> extends AppCompatActivity {

    City c = new City();

    jsonData jdata = new jsonData();
    String s1;

    TextView dname, drank, dLat, dLong;
    EditText nametxt, ranktxt;
    GridLayout glt2;
    LocationServices fusedLocationClient;



    private LocationManager locationManager;
    private String provider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONObject jobj, jobj1;

        try {
            boolean isValidJson = JSONUtils.isJSONValid(jdata.paris);
            if (isValidJson) {
                jobj = new JSONObject(jdata.paris);
                jobj1 = new JSONObject(jobj.getString("city"));

                c.name = jobj1.getString("name");
                c.rank = jobj1.getInt("rank");
            } else {
                Log.e("E:", "IsValidJSON: " + isValidJson);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("E:City Name", c.name);
        Log.i("E:City Rank", c.rank + "");

        getViewById();
        Button buttonAddDetails = (Button) findViewById(R.id.button_add_details);

        buttonAddDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Log.i("E", "Hello World");
                add(glt2);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;

        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            Log.i("E", "location is not null ");
            onLocationChanged(location);
        } else {

            Log.i("E", "location is null ");
        }
    }


    public void onLocationChanged(Location location) {
        int lat = (int) (location.getLatitude());
        int lon = (int) (location.getLongitude());

        Log.i("E", "lat " + lat + "lon " + lon);
    }

    public void getViewById() {
        dname = (TextView) findViewById(R.id.textview_name);
        drank = (TextView) findViewById(R.id.textview_rank);

        dLat = (TextView) findViewById(R.id.textview_latitudeDetails);
        dLong = (TextView) findViewById(R.id.textview_longitudeDetails);

        nametxt = (EditText) findViewById(R.id.edittext_name);
        ranktxt = (EditText) findViewById(R.id.edittext_rank);
        glt2 = (GridLayout) findViewById(R.id.grid_layout1);
//
    }

    public void add(View view) {
        glt2.setVisibility(View.VISIBLE);
        dname.setText(nametxt.getText());
        drank.setText(ranktxt.getText());
        fusedLocationClient = (LocationServices) getFusedLocationProviderClient(this);

        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);

            Log.i("Elaine", "permission not granted");
        } else {

            Log.i("Elaine", "permission granted");
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;

        }
        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Log.i("E", "location is not null ");
            onLocationChanged(location);

            if (location != null) {

                int lat = (int) (location.getLatitude());
                int lon = (int) (location.getLongitude());

                Log.i("E", "my lat " + lat + "my lon " + lon);
                dLat.setText(String.valueOf(lat));
                dLong.setText(String.valueOf(lon));
            }
        } else {

            Log.i("E", "location is null ");
        }

    }
}