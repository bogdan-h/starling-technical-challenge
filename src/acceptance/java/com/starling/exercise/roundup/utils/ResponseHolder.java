package com.starling.exercise.roundup.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseHolder {

  private ResponseEntity<String> response;

  public void set(ResponseEntity<String> response) {
    this.response = response;
  }

  public Integer geStatusCodeValue() {
    return response.getStatusCodeValue();
  }
}
