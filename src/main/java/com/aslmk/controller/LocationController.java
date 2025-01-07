package com.aslmk.controller;

import com.aslmk.dto.LocationsDto;
import com.aslmk.model.Locations;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.openWeatherApi.CurrentLocationDto;
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

                currentLocationDto.getWeather()[0].setIcon(
                        String.format("https://openweathermap.org/img/w/%s.png",
                                currentLocationDto.getWeather()[0].getIcon())
                );
                userLocationsData.add(currentLocationDto);
            }
        }
        model.addAttribute("locations", userLocationsData);

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
        //CurrentLocationDto currentLocationCoordinates = openWeatherService.getLocationCoordinatesByCityName(city);

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
        @GetMapping("location/{locationId}/delete")
    public String deleteLocation(@PathVariable("locationId") long locationId) {
        locationsService.deleteLocationById(locationId);

        return "redirect:/locations";
    }
}
