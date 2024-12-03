package com.aslmk.controller;

import com.aslmk.model.Locations;
import com.aslmk.service.LocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LocationController {
    private LocationsService locationsService;

    @Autowired
    public LocationController(LocationsService locationsService) {
        this.locationsService = locationsService;
    }

    @GetMapping("/locations")
    public String locationsPage(Model model) {
        List<Locations> locationsList = locationsService.getLocations();

        model.addAttribute("locations", locationsList);

        return "locations";
    }

    @GetMapping("/location/add")
    public String addLocation(Model model) {
        model.addAttribute("location", new Locations());
        return "location-create";
    }

}
