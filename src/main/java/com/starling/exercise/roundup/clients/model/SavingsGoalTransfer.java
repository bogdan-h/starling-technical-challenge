package com.starling.exercise.roundup.clients.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SavingsGoalTransfer {

  private final Amount amount;
}
