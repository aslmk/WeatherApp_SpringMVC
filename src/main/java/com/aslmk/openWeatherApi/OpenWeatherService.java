package com.aslmk.openWeatherApi;

import com.aslmk.util.OpenWeatherApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class OpenWeatherService {

    private final RestTemplate restTemplate;

    @Autowired
    public OpenWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocationCoordinatesResponse getLocationCoordinates(String city) {
        String locationCoordinates = OpenWeatherApiUtil.getLocationByNameUrl(city);
        return restTemplate.getForObject(locationCoordinates, LocationCoordinatesResponse.class);
    }

    public LocationWeatherResponse getLocationWeather(BigDecimal latitude, BigDecimal longitude) {
        String locationWeather = OpenWeatherApiUtil.getWeatherDataByLocationUrl(latitude, longitude);
        return restTemplate.getForObject(locationWeather, LocationWeatherResponse.class);
    }
}
