package com.aslmk.exception;

public class LocationDoesNotExistsException extends RuntimeException {
    public LocationDoesNotExistsException(String message) {
        super(message);
    }
}
