package com.challenge.phonemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidNumberException extends ResponseStatusException {

    public InvalidNumberException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
