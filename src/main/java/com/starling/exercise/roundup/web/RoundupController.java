package com.starling.exercise.roundup.web;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoundupController {

  @PutMapping(value = "/api/v1/roundup")
  public ResponseEntity roundUp() {

    return ok().build();
  }
}
