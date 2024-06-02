package com.example.cekcuaca.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPIService {
    @GET("forecast/daily")
    Call<WeatherResponse> getWeatherForecast(
            @Query("city") String city,
            @Query("days") int days,
            @Query("key") String apiKey
    );
}
