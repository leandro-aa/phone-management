package com.challenge.phonemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecordNotFoundException extends ResponseStatusException {

    public RecordNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
