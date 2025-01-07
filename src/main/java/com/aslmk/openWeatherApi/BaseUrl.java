package com.aslmk.openWeatherApi;

public class BaseUrl {
    private static final String apiKey = "f64a5c6eda435181b8506eb41a331336";


    public static String getBaseWeatherUrl() {
        return "https://api.openweathermap.org/data/2.5/weather?units=metric&appid=" + apiKey;
    }

    public static String getBaseGeocodingUrl() {
        return "http://api.openweathermap.org/geo/1.0/direct?limit=5&appid=" +apiKey;
    }
}
