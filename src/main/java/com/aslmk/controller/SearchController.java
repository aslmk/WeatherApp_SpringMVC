package com.aslmk.controller;

import com.aslmk.dto.LocationsDto;
import com.aslmk.exception.LocationDoesNotExistsException;
import com.aslmk.exception.WeatherApiException;
import com.aslmk.model.Sessions;
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
import org.springframework.web.bind.annotation.PostMapping;

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
    public String locationSearch(@ModelAttribute("location") LocationsDto locationsDto,
                                 Model model,
                                 HttpServletRequest request) {
        try {
            String city = locationsDto.getName();

            GeoCoordinatesDto[] geoCoordinatesDtos = openWeatherService.getLocationsByNameGeoCodingAPI(city);

            List<GeoCoordinatesDto> locations = new ArrayList<>(Arrays.asList(geoCoordinatesDtos));

            model.addAttribute("locations", locations);
        } catch (LocationDoesNotExistsException | WeatherApiException e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("searchLocation", new LocationsDto());

        String sessionId = CookieUtil.getSessionIdFromCookie(request);
        Sessions dbSession = (sessionId != null) ? sessionService.getValidSession(sessionId) : null;
        if (dbSession != null) {
            model.addAttribute("userName", dbSession.getUser().getLogin());
        }

        return "searched-locations";
    }
}
