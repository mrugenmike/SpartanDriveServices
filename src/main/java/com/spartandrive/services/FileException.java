package com.spartandrive.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mrugen on 11/28/15.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class FileException extends RuntimeException {
    public FileException(String message) {
        super(message);
    }
}
