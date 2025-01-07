package com.aslmk.controller;

import com.aslmk.dto.LocationsDto;
import com.aslmk.openWeatherApi.GeoCoordinatesDto;
import com.aslmk.openWeatherApi.OpenWeatherService;
import com.aslmk.service.LocationsService;
import com.aslmk.service.SessionService;
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
    private OpenWeatherService openWeatherService;

    @Autowired
    public void SearchController(OpenWeatherService openWeatherService) {
        this.openWeatherService = openWeatherService;
    }

    @GetMapping("/location/search")
    public String searchPage(Model model) {
        model.addAttribute("location", new LocationsDto());
        return "search-page";
    }
    @PostMapping("/location/search")
    public String locationSearch(@ModelAttribute("location") LocationsDto locationsDto,
                                 Model model) {

        String city = locationsDto.getName();

        GeoCoordinatesDto[] geoCoordinatesDtos = openWeatherService.getLocationsByNameGeoCodingAPI(city);

        List<GeoCoordinatesDto> locations = new ArrayList<>(Arrays.asList(geoCoordinatesDtos));

        model.addAttribute("locations", locations);

        return "searched-locations";
    }
}
