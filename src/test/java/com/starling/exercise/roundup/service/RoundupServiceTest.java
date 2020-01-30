package com.starling.exercise.roundup.service;

import static com.starling.exercise.roundup.clients.model.TransactionFeedItems.TransactionFeedItemDirection.OUT;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.starling.exercise.roundup.clients.AccountsClient;
import com.starling.exercise.roundup.clients.SavingsGoalClient;
import com.starling.exercise.roundup.clients.TransactionFeedClient;
import com.starling.exercise.roundup.clients.model.Accounts;
import com.starling.exercise.roundup.clients.model.Accounts.Account;
import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems.TransactionFeedItem;
import com.starling.exercise.roundup.web.model.Error;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoundupServiceTest {

  private final UUID accountUid = randomUUID();
  private final UUID savingsGoalUid = randomUUID();
  private final UUID defaultCategoryUid = randomUUID();
  private final Account account = Account.builder().defaultCategory(defaultCategoryUid).build();
  private final Amount amount = Amount.builder().minorUnits(75).build();
  private final TransactionFeedItem feedItem = TransactionFeedItem.builder().direction(OUT).amount(amount).build();
  private final TransactionFeedItems feedItems = TransactionFeedItems.builder().feedItems(List.of(feedItem)).build();
  private final Amount roundupAmount = Amount.builder().minorUnits(25).build();
  private final OffsetDateTime minTransactionTimestamp = now();
  private final OffsetDateTime maxTransactionTimestamp = now();
  @Mock
  private AccountsClient accountsClient;
  @Mock
  private TransactionFeedClient transactionFeedClient;
  @Mock
  private SavingsGoalClient savingsGoalClient;
  @InjectMocks
  private RoundupService roundupService;

  @Test
  @DisplayName("should get transaction feed and add to savings goal if there is exactly one account")
  void roundupForOneAccount() {
    Accounts accounts = Accounts.builder().accounts(List.of(account)).build();
    when(accountsClient.accounts()).thenReturn(accounts);

    when(transactionFeedClient
        .transactionFeed(accountUid, defaultCategoryUid, minTransactionTimestamp, maxTransactionTimestamp))
        .thenReturn(feedItems);

    roundupService.roundup(accountUid, savingsGoalUid, minTransactionTimestamp, maxTransactionTimestamp);

    verify(transactionFeedClient)
        .transactionFeed(accountUid, defaultCategoryUid, minTransactionTimestamp, maxTransactionTimestamp);

    verify(savingsGoalClient).addMoney(accountUid, savingsGoalUid, roundupAmount);
  }

  @Test
  @DisplayName("should get transaction feed and add to savings goal if there is more than one account")
  void roundupForMultipleAccounts() {
    Account account2 = Account.builder().defaultCategory(randomUUID()).build();
    Accounts accounts = Accounts.builder().accounts(List.of(account, account2)).build();
    when(accountsClient.accounts()).thenReturn(accounts);

    when(transactionFeedClient
        .transactionFeed(accountUid, defaultCategoryUid, minTransactionTimestamp, maxTransactionTimestamp))
        .thenReturn(feedItems);

    roundupService.roundup(accountUid, savingsGoalUid, minTransactionTimestamp, maxTransactionTimestamp);

    verify(transactionFeedClient)
        .transactionFeed(accountUid, defaultCategoryUid, minTransactionTimestamp, maxTransactionTimestamp);

    verify(savingsGoalClient).addMoney(accountUid, savingsGoalUid, roundupAmount);
  }

  @Test
  @DisplayName("should not get transaction feed and not add to savings goal if there is no account")
  void roundupForZeroAccounts() {
    Accounts accounts = Accounts.builder().accounts(emptyList()).build();
    when(accountsClient.accounts()).thenReturn(accounts);

    StarlingOperation result = roundupService
        .roundup(accountUid, savingsGoalUid, minTransactionTimestamp, maxTransactionTimestamp);

    verifyZeroInteractions(transactionFeedClient);
    verifyZeroInteractions(savingsGoalClient);

    assertThat(result.getSuccess()).isFalse();
    assertThat(result.getErrors()).contains(Error.builder().message("There are zero accounts for this user").build());
  }

}
