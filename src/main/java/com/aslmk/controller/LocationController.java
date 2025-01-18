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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Session> dbSession = sessionService.findById(sessionIdFromCookie);

        if (dbSession.isPresent()) {
            model.addAttribute("userName", dbSession.get().getUser().getLogin());

            long dbSessionUserId = dbSession.get().getUser().getId();
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
        }

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
        Optional<Session> dbSession = sessionService.findById(sessionIdFromCookie);

        if (dbSession.isPresent()) {
            model.addAttribute("userName", dbSession.get().getUser().getLogin());

            try {
                User user = dbSession.get().getUser();
                locationService.save(locationDto, user);
            } catch (LocationAlreadyAddedException e) {
                model.addAttribute("error", e.getMessage());
                model.addAttribute("searchLocation", new LocationDto());
                return "searched-locations";
            }
        }

        return "redirect:/locations";
    }

    @PostMapping("location/{locationId}")
    public String deleteLocation(@PathVariable("locationId") long locationId,
                                 HttpServletRequest request) {

        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(request);

        Optional<Location> targetLocation = locationService.findLocationById(locationId);
        Optional<Session> currentSession = sessionService.findById(sessionIdFromCookie);

        if (currentSession.isPresent() && targetLocation.isPresent()) {
            User currentUser = currentSession.get().getUser();

            if (targetLocation.get().getUser().getId() == currentUser.getId()) {
                locationService.deleteLocationById(locationId);
            }
        }

        return "redirect:/locations";
    }
}
