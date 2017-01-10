package com.pristalovpavel.in4mative.rest.model;

import java.util.List;

/**
 * Created by anil on 10.01.17.
 */

public class OpenWeatherMapData {
    public String message;
    public List<PlaceWeather> list;

    public class PlaceWeather
    {
        public long id;
        public String name;
        public MainData main;
        public WindData wind;
    }

    public class MainData
    {
        public float temp;
        public float pressure;
        public float humidity;
    }

    public class WindData
    {
        public float speed;
        public float deg;
    }
}

