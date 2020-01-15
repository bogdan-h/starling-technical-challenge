package com.starling.exercise.roundup.service;

import com.starling.exercise.roundup.clients.AccountsClient;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundupService {

  private final AccountsClient accountsClient;

  public void roundup(UUID accountUid, UUID savingsGoalUid, OffsetDateTime minTransactionTimestamp,
      OffsetDateTime maxTransactionTimestamp) {

    accountsClient.accounts();
  }
}
