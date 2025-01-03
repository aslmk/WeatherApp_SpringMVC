package com.aslmk.controller;

import com.aslmk.dto.LocationsDto;
import com.aslmk.model.Locations;
import com.aslmk.model.Sessions;
import com.aslmk.model.Users;
import com.aslmk.service.LocationsService;
import com.aslmk.service.SessionService;
import com.aslmk.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SearchController {
    private LocationsService locationsService;
    private SessionService sessionService;

    @Autowired
    public void SearchController(LocationsService locationsService,
                                 SessionService sessionService) {
        this.locationsService = locationsService;
        this.sessionService = sessionService;
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
