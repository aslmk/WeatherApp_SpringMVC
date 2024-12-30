package com.aslmk.controller;

import com.aslmk.dto.LocationsDto;
import com.aslmk.model.Locations;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.openWeatherApi.LocationWeatherResponse;
import com.aslmk.openWeatherApi.OpenWeatherService;
import com.aslmk.openWeatherApi.LocationCoordinatesResponse;
import com.aslmk.service.LocationsService;
import com.aslmk.service.SessionService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class LocationController {
    private LocationsService locationsService;
    private OpenWeatherService openWeatherService;
    private SessionService sessionService;

    @Autowired
    public LocationController(LocationsService locationsService,
                              OpenWeatherService openWeatherService,
                              SessionService sessionService) {
        this.locationsService = locationsService;
        this.openWeatherService = openWeatherService;
        this.sessionService = sessionService;
    }

    @GetMapping("/locations")
    public String locationsPage(Model model, HttpServletRequest request) {
        List<Locations> userLocations = Collections.emptyList();

        String sessionId = CookieUtil.getSessionIdFromCookie(request);
        Sessions dbSession = (sessionId != null) ? sessionService.getValidSession(sessionId) : null;

        if (dbSession != null) {
            Users sessionUser = dbSession.getUser();
            userLocations = locationsService.getLocationsByUserId(sessionUser.getId());
        }
        model.addAttribute("locations", userLocations);

        return "locations-page";
    }

    @GetMapping("/location/add")
    public String addLocationForm(Model model) {
        model.addAttribute("location", new LocationsDto());
        return "location-create";
    }

    @PostMapping("/location/add")
    public String addLocation(@ModelAttribute("location") LocationsDto locationsDto,
                              HttpSession session) {

        String city = locationsDto.getName();
        Users user = null;

        LocationCoordinatesResponse locationCoordinatesResponse = openWeatherService.getLocationCoordinates(city);

        locationsDto.setLongitude(locationCoordinatesResponse.getCoord().getLon());
        locationsDto.setLatitude(locationCoordinatesResponse.getCoord().getLat());
        Sessions sessions = sessionService.findById(session.getId());


        if (sessions != null) user = sessions.getUser();
        // simple log here
        else System.out.println("Session not found");

        if (user != null) locationsService.save(locationsDto, user);
        // also simple log below ...
        else System.out.println("User not found");


        return "redirect:/locations";
    }

    @GetMapping("/location-detail/{locationId}")
    public String locationDetail(@PathVariable("locationId") int locationId,
                                 Model model) {

        Locations locationCoordinates = locationsService.findLocationById(locationId);
        LocationWeatherResponse locationWeatherResponse;


        if (locationCoordinates != null) {
            locationWeatherResponse = openWeatherService.getLocationWeather(
                    locationCoordinates.getLatitude(),
                    locationCoordinates.getLongitude()
            );
            String imgUrl = String.format("https://openweathermap.org/img/w/%s.png",
                    locationWeatherResponse.getWeather()[0].getIcon());
            model.addAttribute("iconImg", imgUrl);
            model.addAttribute("locationName", locationCoordinates.getName());
            model.addAttribute("weather", locationWeatherResponse.getWeather()[0]);
        }

        return "location-details";
    }

    @GetMapping("location/{locationId}/delete")
    public String deleteLocation(@PathVariable("locationId") long locationId) {
        locationsService.deleteLocationById(locationId);

        return "redirect:/locations";
    }

    @GetMapping("/location/search")
    public String searchPage(Model model) {
        model.addAttribute("location", new LocationsDto());
        return "search-page";
    }
    @PostMapping("/location/search")
    public String locationSearch(@ModelAttribute("location") LocationsDto locationsDto,
                                 HttpServletRequest request,
                                 Model model) {

        String city = locationsDto.getName();
        Locations location = null;
        String sessionId = CookieUtil.getSessionIdFromCookie(request);
        Sessions dbSession = (sessionId != null) ? sessionService.getValidSession(sessionId) : null;

        if (dbSession != null) {
            Users sessionUser = dbSession.getUser();
            location = locationsService.findLocationByCityName(sessionUser, city);
        }

        model.addAttribute("location", location);

        return "searched-locations";
    }
}
