package com.starling.exercise.roundup.clients.model;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Accounts {

  private final List<Account> accounts;

  @Value
  @Builder
  public static class Account {

    private final UUID defaultCategory;
  }
}
