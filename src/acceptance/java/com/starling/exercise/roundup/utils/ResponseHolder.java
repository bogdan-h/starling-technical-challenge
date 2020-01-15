package com.starling.exercise.roundup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseHolder {

  public static final ObjectMapper objectMapper = new ObjectMapper();

  private ResponseEntity<String> response;

  public void set(ResponseEntity<String> response) {
    this.response = response;
  }

  public Integer geStatusCodeValue() {
    return response.getStatusCodeValue();
  }
}
