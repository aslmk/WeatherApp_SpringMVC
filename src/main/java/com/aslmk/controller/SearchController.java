package com.aslmk.controller;

import com.aslmk.dto.LocationDto;
import com.aslmk.model.Session;
import com.aslmk.openWeatherApi.GeoCoordinatesDto;
import com.aslmk.openWeatherApi.OpenWeatherService;
import com.aslmk.service.SessionService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {
    private final OpenWeatherService openWeatherService;
    private final SessionService sessionService;

    public SearchController(OpenWeatherService openWeatherService, SessionService sessionService) {
        this.openWeatherService = openWeatherService;
        this.sessionService = sessionService;
    }


    @GetMapping("/location/search")
    public String locationSearch(@ModelAttribute("location") LocationDto locationDto,
                                 Model model,
                                 HttpServletRequest request) {

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);
        Optional<Session> dbSession = sessionService.findById(sessionIdFromCookie);

        if (dbSession.isPresent()) {
            model.addAttribute("userName", dbSession.get().getUser().getLogin());

            String city = locationDto.getName();

            GeoCoordinatesDto[] geoCoordinatesDtos = openWeatherService.getLocationsByNameGeoCodingAPI(city);

            List<GeoCoordinatesDto> locations = new ArrayList<>(Arrays.asList(geoCoordinatesDtos));

            model.addAttribute("locations", locations);
        }

        model.addAttribute("searchLocation", new LocationDto());

        return "searched-locations";
    }
}
