package com.aslmk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LocationAlreadyAddedException extends RuntimeException {
    public LocationAlreadyAddedException(String message) {
        super(message);
    }
}
