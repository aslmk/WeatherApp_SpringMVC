package com.aslmk.controller;

import com.aslmk.dto.LocationDto;
import com.aslmk.exception.LocationDoesNotExistsException;
import com.aslmk.exception.WeatherApiException;
import com.aslmk.model.Session;
import com.aslmk.openWeatherApi.GeoCoordinatesDto;
import com.aslmk.openWeatherApi.OpenWeatherService;
import com.aslmk.service.SessionService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class SearchController {
    private final OpenWeatherService openWeatherService;
    private final SessionService sessionService;

    @Autowired
    public SearchController(OpenWeatherService openWeatherService, SessionService sessionService) {
        this.openWeatherService = openWeatherService;
        this.sessionService = sessionService;
    }


    @GetMapping("/location/search")
    public String locationSearch(@ModelAttribute("location") LocationDto locationDto,
                                 Model model,
                                 HttpServletRequest request) {

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);
        Session dbSession = sessionService.findById(sessionIdFromCookie);

        if (dbSession == null) {
            model.addAttribute("error", "Session not found");
            return "searched-locations";
        }

        model.addAttribute("userName", dbSession.getUser().getLogin());

        try {
            String city = locationDto.getName();

            GeoCoordinatesDto[] geoCoordinatesDtos = openWeatherService.getLocationsByNameGeoCodingAPI(city);

            List<GeoCoordinatesDto> locations = new ArrayList<>(Arrays.asList(geoCoordinatesDtos));

            model.addAttribute("locations", locations);

        } catch (LocationDoesNotExistsException | WeatherApiException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("searchLocation", new LocationDto());

        return "searched-locations";
    }
}
