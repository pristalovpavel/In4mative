package com.pristalovpavel.in4mative.rest.interfaces;

import com.pristalovpavel.in4mative.rest.model.OpenWeatherMapData;

import retrofit.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by anil on 10.01.17.
 */

public interface OpenWeatherMapAPI {
    @GET("/data/2.5/find")
    Call<OpenWeatherMapData> loadQuestions(@Query("lat") double lat, @Query("lon") double lon);
}
