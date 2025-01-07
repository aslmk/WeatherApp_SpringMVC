package com.aslmk.util;


import com.aslmk.openWeatherApi.BaseUrl;

import java.math.BigDecimal;

public class OpenWeatherApiUtil {
    public static String getLocationByNameUrl(String city) {
        return BaseUrl.getBaseWeatherUrl() + "&q=" + city;
    }

    public static String getWeatherDataByLocationUrl(BigDecimal lat, BigDecimal lon) {
        return BaseUrl.getBaseWeatherUrl() + "&lat=" + lat.toString() + "&lon=" + lon.toString();
    }

    public static String getLocationsByNameUrl(String city) {
        return BaseUrl.getBaseGeocodingUrl() + "&q=" + city;
    }

}
