package com.spartandrive.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by mrugen on 11/21/15.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PushException extends RuntimeException {
    private final String message;

    public PushException(String message){
        this.message = message;
    }
}
