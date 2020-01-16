package com.starling.exercise.roundup.web.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StarlingOperation {

  private final Boolean success;
  private final List<Error> errors;
}
