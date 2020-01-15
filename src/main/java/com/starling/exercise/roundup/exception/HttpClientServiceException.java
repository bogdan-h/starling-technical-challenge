package com.starling.exercise.roundup.exception;

import org.springframework.http.HttpStatus;

public class HttpClientServiceException extends RuntimeException {

    private final HttpStatus status;

    public HttpClientServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
