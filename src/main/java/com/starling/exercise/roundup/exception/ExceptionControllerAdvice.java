package com.starling.exercise.roundup.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.status;

import com.starling.exercise.roundup.web.model.Error;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("com.starling.exercise.roundup")
public class ExceptionControllerAdvice {

  @ExceptionHandler(HttpClientServiceException.class)
  public ResponseEntity<StarlingOperation> handleCreditCardsException(HttpClientServiceException ex) {
    final Error error = Error.builder().message(ex.getMessage()).build();
    final StarlingOperation response = StarlingOperation.builder()
        .success(false)
        .errors(List.of(error))
        .build();
    return status(ex.getStatus()).contentType(APPLICATION_JSON).body(response);
  }
}
