package com.aslmk.controller;

import com.aslmk.dto.LocationsDto;
import com.aslmk.exception.LocationAlreadyAddedException;
import com.aslmk.exception.SessionNotFoundException;
import com.aslmk.model.Locations;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.openWeatherApi.CurrentLocationDto;
import com.aslmk.openWeatherApi.OpenWeatherService;
import com.aslmk.service.LocationsService;
import com.aslmk.service.SessionService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LocationController {
    private final LocationsService locationsService;
    private final OpenWeatherService openWeatherService;
    private final SessionService sessionService;

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
        List<CurrentLocationDto> userLocationsData = new ArrayList<>();

        String sessionId = CookieUtil.getSessionIdFromCookie(request);
        Sessions dbSession = (sessionId != null) ? sessionService.getValidSession(sessionId) : null;

        if (dbSession != null) {
            long dbSessionUserId = dbSession.getUser().getId();
            List<Locations> userLocations = locationsService.getLocationsByUserId(dbSessionUserId);

            for (Locations location : userLocations) {
                CurrentLocationDto currentLocationDto = openWeatherService.getLocationWeatherByCoordinates(
                        location.getLatitude(), location.getLongitude()
                );
                // Setting id from database, not from openWeather API.
                // Otherwise, this location will not be deleted from db.
                currentLocationDto.setId(location.getId());

                userLocationsData.add(currentLocationDto);
            }
        }
        model.addAttribute("searchLocation", new LocationsDto());
        model.addAttribute("locations", userLocationsData);

        return "locations-page";
    }


    @PostMapping("/locations")
    public String addLocation(@ModelAttribute("location") LocationsDto locationsDto,
            @RequestParam("name") String name,
            @RequestParam("lat") BigDecimal lat,
            @RequestParam("lon") BigDecimal lon,
                              HttpSession session,
                              Model model) {

        locationsDto.setName(name);
        locationsDto.setLongitude(lon);
        locationsDto.setLatitude(lat);

        try {
            Sessions sessions = sessionService.findById(session.getId());
            Users user = sessions.getUser();
            locationsService.save(locationsDto, user);

        } catch (LocationAlreadyAddedException | SessionNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("searchLocation", new LocationsDto());
            return "searched-locations";
        }

        return "redirect:/locations";
    }

    @PostMapping("location/{locationId}")
    public String deleteLocation(@PathVariable("locationId") long locationId,
                                 HttpServletRequest request) {

        String sessionId = CookieUtil.getSessionIdFromCookie(request);

        Locations targetLocation = locationsService.findLocationById(locationId);
        Sessions currentSession = sessionService.findById(sessionId);
        Users currentUser = currentSession.getUser();

        if (targetLocation.getUser().getId() == currentUser.getId()) {
            locationsService.deleteLocationById(locationId);
        }

        return "redirect:/locations";
    }
}
