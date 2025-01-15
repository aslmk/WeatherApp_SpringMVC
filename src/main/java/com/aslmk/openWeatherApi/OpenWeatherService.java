package com.aslmk.openWeatherApi;

import com.aslmk.exception.LocationDoesNotExistsException;
import com.aslmk.exception.WeatherApiException;
import com.aslmk.util.OpenWeatherApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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
        try {
            return restTemplate.getForObject(locationCoordinates, LocationCoordinatesResponse.class);
        } catch (RestClientException e) {
            throw new WeatherApiException("Error occurred while fetching location data: " + e.getMessage());
        }
    }

    public CurrentLocationDto getLocationWeatherByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        String locationWeather = OpenWeatherApiUtil.getWeatherDataByLocationUrl(latitude, longitude);
        try {
            return restTemplate.getForObject(locationWeather, CurrentLocationDto.class);
        } catch (RestClientException e) {
            throw new WeatherApiException("Error occurred while fetching location data: " + e.getMessage());
        }
    }

    public GeoCoordinatesDto[] getLocationsByNameGeoCodingAPI(String city) throws LocationDoesNotExistsException {
        String locations = OpenWeatherApiUtil.getLocationsByNameUrl(city);
        try {
            GeoCoordinatesDto[] locationsCoordinates = restTemplate.getForObject(locations, GeoCoordinatesDto[].class);
            return validateLocations(locationsCoordinates);
        } catch (RestClientException e) {
            throw new WeatherApiException("Error occurred while fetching location data: " + e.getMessage());
        }
    }

    private GeoCoordinatesDto[] validateLocations(GeoCoordinatesDto[] locationsCoordinates) {
        if (locationsCoordinates == null || locationsCoordinates.length == 0) {
            throw new LocationDoesNotExistsException("There is no location with such name!");
        }
        return locationsCoordinates;
    }
}
