package com.starling.exercise.roundup.web.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Error {

  private final String message;
}
