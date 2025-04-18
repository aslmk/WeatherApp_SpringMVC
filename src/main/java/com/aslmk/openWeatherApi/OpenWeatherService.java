package com.aslmk.openWeatherApi;

import com.aslmk.exception.LocationDoesNotExistsException;
import com.aslmk.exception.WeatherApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Service
public class OpenWeatherService {

    @Value("${openWeather.api.key}")
    private String API_KEY;
    private final String BASE_API_URL = "https://api.openweathermap.org";
    private final String BASE_WEATHER_URL = "/data/2.5/weather";
    private final String BASE_GEOCODING_URL = "/geo/1.0/direct";

    private final RestTemplate restTemplate;

    @Autowired
    public OpenWeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocationCoordinatesResponse getLocationCoordinates(String city) {
        String locationCoordinates = buildLocationUrlByCityName(city);
        try {
            return restTemplate.getForObject(locationCoordinates, LocationCoordinatesResponse.class);
        } catch (RestClientException e) {
            throw new WeatherApiException("Error occurred while fetching location data: " + e.getMessage());
        }
    }

    public CurrentLocationDto getLocationWeatherByCoordinates(BigDecimal latitude, BigDecimal longitude) {
        String locationWeather = buildWeatherDataUrlByLocationCoordinates(latitude, longitude);
        try {
            return restTemplate.getForObject(locationWeather, CurrentLocationDto.class);
        } catch (RestClientException e) {
            throw new WeatherApiException("Error occurred while fetching location data: " + e.getMessage());
        }
    }

    public GeoCoordinatesDto[] getLocationsByNameGeoCodingAPI(String city) throws LocationDoesNotExistsException, WeatherApiException {
        if (city == null || city.trim().isEmpty()) {
            throw new LocationDoesNotExistsException("City name cannot be empty.");
        }

        String locations = buildLocationsCoordinatesListUrlByCityName(city);
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
    private String buildLocationUrlByCityName(String city) {
        return BASE_API_URL + BASE_WEATHER_URL + "?units=metric" + "&appid=" + API_KEY + "&q=" + city;
    }
    private String buildWeatherDataUrlByLocationCoordinates(BigDecimal lat, BigDecimal lon) {
        return BASE_API_URL + BASE_WEATHER_URL + "?units=metric" + "&appid=" + API_KEY +
                "&lat=" + lat.toString() + "&lon=" + lon.toString();
    }
    private String buildLocationsCoordinatesListUrlByCityName(String city) {
        return BASE_API_URL + BASE_GEOCODING_URL + "?limit=5" + "&appid=" + API_KEY + "&q=" + city;
    }
}
