package com.starling.exercise.roundup.clients.model;

import java.util.List;
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
  }
}
