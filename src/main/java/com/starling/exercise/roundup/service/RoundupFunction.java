package com.starling.exercise.roundup.service;

import static com.starling.exercise.roundup.clients.model.TransactionFeedItems.TransactionFeedItemDirection.OUT;

import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems;

@FunctionalInterface
public interface RoundupFunction {

  static RoundupFunction roundup(TransactionFeedItems feedItems) {
    return () -> {
      final int sum = feedItems.getFeedItems().stream()
          .filter(item -> item.getDirection().equals(OUT))
          .mapToInt(item -> item.getAmount().getMinorUnits())
          .filter(amount -> amount >= 0)
          .map(amount -> 100 - amount % 100)
          .filter(amount -> amount != 100)
          .sum();
      final String currency = feedItems.getFeedItems().get(0).getAmount().getCurrency();
      return Amount.builder().currency(currency).minorUnits(sum).build();
    };
  }

  Amount apply();
}