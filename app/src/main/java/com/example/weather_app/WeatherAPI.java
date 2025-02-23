package com.example.weather_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather")  // The endpoint for current weather
    Call<WeatherResponse> getWeather(
            @Query("q") String city,  // City name
            @Query("appid") String apiKey  // Your API key
    );
}


