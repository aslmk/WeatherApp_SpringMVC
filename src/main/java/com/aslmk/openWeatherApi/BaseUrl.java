package com.aslmk.openWeatherApi;

public class BaseUrl {
    private String baseUrl;
    private static final String apiKey = "f64a5c6eda435181b8506eb41a331336";


    public static String getBaseUrl() {
        return "https://api.openweathermap.org/data/2.5/weather?appid=" + apiKey;
    }
}
