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
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity<LocationServices> extends AppCompatActivity {

    City c = new City();

    jsonData jdata = new jsonData();
    String s1;


    TextView dname, drank, dLat, dLong, dTemp;
    EditText nametxt, ranktxt;
    GridLayout glt2;

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

        getViewById();
        Button buttonAddDetails = (Button) findViewById(R.id.button_add_details);

        buttonAddDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

        Log.i("E", "Location Changed: lat " + lat + "lon " + lon);
    }

    public void getViewById() {
        dname = (TextView) findViewById(R.id.textview_cityNameDetails);
        drank = (TextView) findViewById(R.id.textview_rankResponseDetails);

        dLat = (TextView) findViewById(R.id.textview_latitudeDetails);
        dLong = (TextView) findViewById(R.id.textview_longitudeDetails);
        dTemp = (TextView) findViewById(R.id.textview_temperatureDetails);

        nametxt = (EditText) findViewById(R.id.edittext_name);
        ranktxt = (EditText) findViewById(R.id.edittext_rank);
        glt2 = (GridLayout) findViewById(R.id.grid_layout1);

    }

    public void resetPlaceholders(){
        dname.setText(getString(R.string.placeholder));
        drank.setText(getString(R.string.placeholder));
        dLat.setText(getString(R.string.placeholder));
        dLong.setText(getString(R.string.placeholder));
        dTemp.setText(getString(R.string.placeholder));
    }
    public void add(View view) {
        glt2.setVisibility(View.VISIBLE);

        int rank = 0;
        c.name = nametxt.getText().toString();
        String rankval =ranktxt.getText().toString();

        try {
            rank =Integer.parseInt(rankval);
        } catch(NumberFormatException e) {
            Log.i("E", e.getMessage());
            rank = 0;
        } catch(NullPointerException e) {
            Log.i("E", e.getMessage());
            rank = 0;
        }

        c.rank = rank;

        postData(String.valueOf(nametxt.getText()));

    }

    private void postData(String cityname) {
        Log.e("E", "PostData : " + cityname);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org").addConverterFactory(GsonConverterFactory.create()).build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Map<String, String> data = new HashMap<>();
        data.put("q", cityname);
        data.put("appid", "bec2ea2f434c848c09196f2de96e3c4c");

        Call<Object> call = retrofitAPI.getWeather(data);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {

                JSONObject jobject, tempobj;
                Toast.makeText(MainActivity.this, "Data added to API", Toast.LENGTH_SHORT).show();

                nametxt.setText("");
                ranktxt.setText("");


                String responsebody =new Gson().toJson(response.body());
                try{
                    Log.e("E", "responsebody  = "+responsebody);
                    JSONObject jobjresponse = new JSONObject(responsebody);
                    c.name = jobjresponse.getString("name");
                    jobject=new JSONObject(jobjresponse.getString("coord"));
                    c.latitude=jobject.getDouble("lat");
                    c.longitude=jobject.getDouble("lon");
                    tempobj=new JSONObject(jobjresponse.getString("main"));
                    c.temperature=tempobj.getDouble("temp");
                    Log.i("City name", c.name);



                }catch(JSONException e){
                    e.printStackTrace();
                }

                Log.i("City res name", c.name);
                Log.i("City res rank", c.rank+"");
                Log.i("City res lat", c.latitude+"");
                Log.i("City res lon", c.longitude+"");
                Log.i("City res temp", c.getCelcius()+"");

                if(c.isSet()){
                    dname.setText(c.name);
                    drank.setText(""+c.rank);
                    dLat.setText(""+c.latitude);
                    dLong.setText(""+c.longitude);
                    dTemp.setText(""+c.getCelcius());
                }else {
                    resetPlaceholders();
                }

                c.reset();

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
              Log.i("E :  ", t.getMessage());
                Toast.makeText(MainActivity.this, "Error "+t.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }
}

