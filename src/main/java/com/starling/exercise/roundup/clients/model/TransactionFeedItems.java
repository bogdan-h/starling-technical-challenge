package com.starling.exercise.roundup.clients.model;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionFeedItems {

  private final List<TransactionFeedItem> feedItems;

  @Value
  @Builder
  public static class TransactionFeedItem {

    private final Amount amount;
    private final TransactionFeedItemDirection direction;
  }

  @AllArgsConstructor
  public enum TransactionFeedItemDirection {
    IN("IN"),
    OUT("OUT");

    @JsonValue
    private final String direction;
  }
}
