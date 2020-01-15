package com.starling.exercise.roundup.exception;

import static org.springframework.http.ResponseEntity.status;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.starling.exercise.roundup")
public class ExceptionControllerAdvice {

    @ExceptionHandler(HttpClientServiceException.class)
    public ResponseEntity handleCreditCardsException(HttpClientServiceException ex) {
        return status(ex.getStatus()).build();
    }
}
