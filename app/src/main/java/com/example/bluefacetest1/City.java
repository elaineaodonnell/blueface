package com.example.bluefacetest1;
import android.location.Location;

public class City {
    public String name;
    public int rank;
    public double temperature;
    public double latitude;
    public double longitude;
    final double kelvin;

    {
        kelvin = 273.15;
    }

    public double getCelcius(){
        return Math.round(temperature-kelvin);
    }

    public void reset(){
        name = "";
        rank = 0;
        temperature = 0.0;
        latitude = 0.0;
        longitude = 0.0;
    }

    public boolean isSet(){
        if(getCelcius()>-100)return true;
        return false;
    }
}
