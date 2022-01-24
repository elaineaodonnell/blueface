package com.example.bluefacetest1;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitAPI {

    @GET("data/2.5/weather")
    Call<Object> getWeather(@QueryMap Map<String, String> options);
}
