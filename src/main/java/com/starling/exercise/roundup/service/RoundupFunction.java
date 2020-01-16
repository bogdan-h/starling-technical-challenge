package com.starling.exercise.roundup.service;

import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems;

@FunctionalInterface
public interface RoundupFunction {

  static RoundupFunction roundup(TransactionFeedItems feedItems) {
    return () -> {
      final int sum = feedItems.getFeedItems().stream()
          .mapToInt(item -> item.getAmount().getMinorUnits())
          .filter(amount -> amount >= 0)
          .map(amount -> 100 - amount % 100)
          .filter(amount -> amount != 100)
          .sum();
      return Amount.builder().minorUnits(sum).build();
    };
  }

  Amount apply();
}