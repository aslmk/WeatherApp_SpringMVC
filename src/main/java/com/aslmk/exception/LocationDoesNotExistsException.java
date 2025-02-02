package com.aslmk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LocationDoesNotExistsException extends RuntimeException {
    public LocationDoesNotExistsException(String message) {
        super(message);
    }
}
