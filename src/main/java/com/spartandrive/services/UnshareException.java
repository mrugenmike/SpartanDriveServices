package com.spartandrive.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mrugen on 11/29/15.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnshareException extends Throwable {
    public UnshareException(String message) {
        super(message);
    }
}
