package com.challenge.phonemanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RecordAlreadyExistsException extends ResponseStatusException {

    public RecordAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
