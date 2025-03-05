package com.aslmk.exception;

import com.aslmk.dto.LocationDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({LocationAlreadyAddedException.class})
    public String locationAlreadyAdded(LocationAlreadyAddedException e, Model model) {

        model.addAttribute("error", e.getMessage());
        model.addAttribute("searchLocation", new LocationDto());
        return "searched-locations";

    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public String userAlreadyExists(UserAlreadyExistsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "registration";
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public String invalidCredentials(InvalidCredentialsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "registration";
    }

    @ExceptionHandler({WeatherApiException.class})
    public String weatherApiException(WeatherApiException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("searchLocation", new LocationDto());
        return "searched-locations";
    }

    @ExceptionHandler({LocationDoesNotExistsException.class})
    public String locationDoesNotExists(LocationDoesNotExistsException e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("searchLocation", new LocationDto());
        return "searched-locations";
    }

}
