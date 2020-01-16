package com.starling.exercise.roundup.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.starling.exercise.roundup.utils.AcceptanceTestContext.objectMapper;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.starling.exercise.roundup.clients.model.Accounts;
import com.starling.exercise.roundup.clients.model.Accounts.Account;
import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.SavingsGoalTransfer;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems.TransactionFeedItem;
import com.starling.exercise.roundup.utils.AcceptanceTestContext;
import com.starling.exercise.roundup.web.model.Error;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StarlingStubs {

  private static final String ACCOUNTS_PATH = "/api/v2/accounts";
  private static final String TRANSACTION_FEED_PATH = "/api/v2/feed/account/(.*)/category/(.*)/transactions-between";
  private static final String SAVINGS_GOAL_PATH = "/api/v2/account/(.*)/savings-goals/(.*)/add-money/(.*)";

  @Autowired
  private AcceptanceTestContext acceptanceTestContext;

  public void verifyAccounts() {
    verify(getRequestedFor(urlEqualTo(ACCOUNTS_PATH))
        .withHeader("Accept", equalTo("application/json"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("Authorization", equalTo("Bearer mock_token")));
  }

  public void stubAccounts(Integer status) throws JsonProcessingException {
    final UUID defaultCategoryUid = randomUUID();
    acceptanceTestContext.setDefaultCategoryUid(defaultCategoryUid);
    final Account account = Account.builder().defaultCategory(defaultCategoryUid).build();
    final Accounts accounts = Accounts.builder().accounts(List.of(account)).build();

    stubFor(get(urlEqualTo(ACCOUNTS_PATH))
        .willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(accounts)).withStatus(status)));
  }

  public void verifyTransactionFeed(String minTransactionTimestamp, String maxTransactionTimestamp) {
    final String transactionFeedPath = format(
        "/api/v2/feed/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s",
        acceptanceTestContext.getAccountUid().toString(), acceptanceTestContext.getDefaultCategoryUid().toString(),
        minTransactionTimestamp, maxTransactionTimestamp);

    verify(getRequestedFor(urlEqualTo(transactionFeedPath))
        .withHeader("Accept", equalTo("application/json"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("Authorization", equalTo("Bearer mock_token")));
  }

  public void stubTransactionFeed(Integer status) throws JsonProcessingException {
    final Amount amount = Amount.builder().minorUnits(1234).build();
    final TransactionFeedItem feedItem = TransactionFeedItem.builder().amount(amount).build();
    final TransactionFeedItems feedItems = TransactionFeedItems.builder().feedItems(List.of(feedItem)).build();

    stubFor(get(urlPathMatching(TRANSACTION_FEED_PATH))
        .willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(feedItems)).withStatus(status)));
  }

  public void stubTransactionFeed(List<Integer> amounts) throws JsonProcessingException {
    final List<TransactionFeedItem> feedItemList = amounts.stream()
        .map(amount -> TransactionFeedItem.builder().amount(Amount.builder().minorUnits(amount).build()).build())
        .collect(toList());

    final TransactionFeedItems feedItems = TransactionFeedItems.builder().feedItems(feedItemList).build();

    stubFor(get(urlPathMatching(TRANSACTION_FEED_PATH))
        .willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(feedItems)).withStatus(200)));
  }

  public void stubSavingsGoal(Integer status) throws JsonProcessingException {
    final StarlingOperation transferResponse = StarlingOperation.builder().success(true).build();

    stubFor(put(urlPathMatching(SAVINGS_GOAL_PATH))
        .willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(transferResponse)).withStatus(status)));
  }

  public void stubSavingsGoalWithFailure(String message) throws JsonProcessingException {
    final Error error = Error.builder().message(message).build();
    final StarlingOperation transferResponse = StarlingOperation.builder()
        .success(false)
        .errors(List.of(error))
        .build();

    stubFor(put(urlPathMatching(SAVINGS_GOAL_PATH))
        .willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(transferResponse)).withStatus(200)));
  }

  public void verifySavingsGoal() {
    final String savingsGoalPath = format(
        "/api/v2/account/%s/savings-goals/%s/add-money/(.*)",
        acceptanceTestContext.getAccountUid().toString(), acceptanceTestContext.getSavingsGoalUid().toString());

    verify(putRequestedFor(urlPathMatching(savingsGoalPath))
        .withHeader("Accept", equalTo("application/json"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("Authorization", equalTo("Bearer mock_token")));
  }

  public void verifySavingsGoal(Integer amount) throws JsonProcessingException {
    Amount transferAmount = Amount.builder().minorUnits(amount).build();
    SavingsGoalTransfer transferRequest = SavingsGoalTransfer.builder().amount(transferAmount).build();

    verify(putRequestedFor(urlPathMatching(SAVINGS_GOAL_PATH))
        .withHeader("Accept", equalTo("application/json"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("Authorization", equalTo("Bearer mock_token"))
        .withRequestBody(equalTo(objectMapper.writeValueAsString(transferRequest))));
  }
}
