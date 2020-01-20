package com.starling.exercise.roundup.exception;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.status;

import com.starling.exercise.roundup.web.model.Error;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice("com.starling.exercise.roundup")
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

  @Override
  public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    final Error error = Error.builder().message(ex.getMessage()).build();
    final StarlingOperation response = StarlingOperation.builder()
        .success(false)
        .errors(List.of(error))
        .build();
    return status(status).contentType(APPLICATION_JSON).body(response);
  }

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
