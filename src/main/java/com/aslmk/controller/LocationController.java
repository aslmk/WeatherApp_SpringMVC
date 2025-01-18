package com.aslmk.controller;

import com.aslmk.dto.LocationDto;
import com.aslmk.exception.LocationAlreadyAddedException;
import com.aslmk.model.Location;
import com.aslmk.model.Session;
import com.aslmk.model.User;
import com.aslmk.openWeatherApi.CurrentLocationDto;
import com.aslmk.openWeatherApi.OpenWeatherService;
import com.aslmk.service.LocationService;
import com.aslmk.service.SessionService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LocationController {
    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;
    private final SessionService sessionService;

    @Autowired
    public LocationController(LocationService locationService,
                              OpenWeatherService openWeatherService,
                              SessionService sessionService) {
        this.locationService = locationService;
        this.openWeatherService = openWeatherService;
        this.sessionService = sessionService;
    }

    @GetMapping("/locations")
    public String locationsPage(Model model, HttpServletRequest request) {
        List<CurrentLocationDto> userLocationsData = new ArrayList<>();

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);
        Session dbSession = sessionService.findById(sessionIdFromCookie);

        if (dbSession == null) {
            model.addAttribute("error", "Session not found");
            return "locations-page";
        }

        model.addAttribute("userName", dbSession.getUser().getLogin());

        long dbSessionUserId = dbSession.getUser().getId();
        List<Location> userLocations = locationService.getLocationsByUserId(dbSessionUserId);

        for (Location location : userLocations) {
            CurrentLocationDto currentLocationDto = openWeatherService.getLocationWeatherByCoordinates(
                    location.getLatitude(), location.getLongitude()
            );
            // Setting id from database, not from openWeather API.
            // Otherwise, this location will not be deleted from db.
            currentLocationDto.setId(location.getId());

            userLocationsData.add(currentLocationDto);
        }

        model.addAttribute("searchLocation", new LocationDto());
        model.addAttribute("locations", userLocationsData);

        return "locations-page";
    }


    @PostMapping("/locations")
    public String addLocation(@ModelAttribute("location") LocationDto locationDto,
            @RequestParam("name") String name,
            @RequestParam("lat") BigDecimal lat,
            @RequestParam("lon") BigDecimal lon,
                              Model model,
                              HttpServletRequest request) {

        locationDto.setName(name);
        locationDto.setLongitude(lon);
        locationDto.setLatitude(lat);

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);
        Session dbSession = sessionService.findById(sessionIdFromCookie);

        if (dbSession == null) {
            model.addAttribute("error", "Session not found");
            return "searched-locations";
        }

        model.addAttribute("userName", dbSession.getUser().getLogin());

        try {
            User user = dbSession.getUser();
            locationService.save(locationDto, user);
        } catch (LocationAlreadyAddedException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("searchLocation", new LocationDto());
            return "searched-locations";
        }

        return "redirect:/locations";
    }

    @PostMapping("location/{locationId}")
    public String deleteLocation(@PathVariable("locationId") long locationId,
                                 HttpServletRequest request) {

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);

        Location targetLocation = locationService.findLocationById(locationId);
        Session currentSession = sessionService.findById(sessionIdFromCookie);

        User currentUser = currentSession.getUser();

        if (targetLocation.getUser().getId() == currentUser.getId()) {
            locationService.deleteLocationById(locationId);
        }

        return "redirect:/locations";
    }
}
