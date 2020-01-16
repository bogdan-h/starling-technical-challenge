package com.starling.exercise.roundup.service;

import com.starling.exercise.roundup.clients.AccountsClient;
import com.starling.exercise.roundup.clients.SavingsGoalClient;
import com.starling.exercise.roundup.clients.TransactionFeedClient;
import com.starling.exercise.roundup.clients.model.Accounts;
import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundupService {

  private final AccountsClient accountsClient;
  private final TransactionFeedClient transactionFeedClient;
  private final SavingsGoalClient savingsGoalClient;

  public StarlingOperation roundup(UUID accountUid, UUID savingsGoalUid, OffsetDateTime minTransactionTimestamp,
      OffsetDateTime maxTransactionTimestamp) {

    final Accounts accounts = accountsClient.accounts();
    final UUID categoryUid = accounts.getAccounts().get(0).getDefaultCategory();
    final TransactionFeedItems feedItems = transactionFeedClient
        .transactionFeed(accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);

    final Amount amount = RoundupFunction.roundup(feedItems).apply();
    return savingsGoalClient.addMoney(accountUid, savingsGoalUid, amount);
  }
}
