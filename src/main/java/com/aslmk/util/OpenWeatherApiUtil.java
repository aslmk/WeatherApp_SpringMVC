package com.aslmk.util;


import com.aslmk.openWeatherApi.BaseUrl;

import java.math.BigDecimal;

public class OpenWeatherApiUtil {
    public static String getLocationByNameUrl(String city) {
        return BaseUrl.getBaseUrl() + "&q=" + city + "&units=metric";
    }

    public static String getWeatherDataByLocationUrl(BigDecimal lat, BigDecimal lon) {
        return BaseUrl.getBaseUrl() + "&lat=" + lat.toString() + "&lon=" + lon.toString() + "&units=metric";
    }

}
