package com.aslmk.util;


import com.aslmk.openWeatherApi.BaseUrl;

import java.math.BigDecimal;

public class OpenWeatherApiUtil {
    public static String buildLocationUrlByCityName(String city) {
        return BaseUrl.getBaseWeatherUrl() + "&q=" + city;
    }

    public static String buildWeatherDataUrlByLocationCoordinates(BigDecimal lat, BigDecimal lon) {
        return BaseUrl.getBaseWeatherUrl() + "&lat=" + lat.toString() + "&lon=" + lon.toString();
    }

    public static String buildLocationsCoordinatesListUrlByCityName(String city) {
        return BaseUrl.getBaseGeocodingUrl() + "&q=" + city;
    }

}
