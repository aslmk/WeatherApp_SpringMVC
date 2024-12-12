package com.aslmk.openWeatherApi;

import com.aslmk.util.OpenWeatherApiUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class OpenWeatherService {



    public LocationCoordinatesResponse getLocationCoordinates(String city) {
        String locationCoordinates = OpenWeatherApiUtil.getLocationByNameUrl(city);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(locationCoordinates, LocationCoordinatesResponse.class);
    }

    public LocationWeatherResponse getLocationWeather(BigDecimal latitude, BigDecimal longitude) {
        String locationWeather = OpenWeatherApiUtil.getWeatherDataByLocationUrl(latitude, longitude);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(locationWeather, LocationWeatherResponse.class);
    }
}
