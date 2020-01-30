package com.starling.exercise.roundup.clients;

import static com.starling.exercise.roundup.clients.model.TransactionFeedItems.TransactionFeedItemDirection.OUT;
import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems;
import com.starling.exercise.roundup.clients.model.TransactionFeedItems.TransactionFeedItem;
import com.starling.exercise.roundup.exception.HttpClientServiceException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

@ExtendWith(SpringExtension.class)
@RestClientTest(TransactionFeedClient.class)
class TransactionFeedClientTest {

  private final Amount amount = Amount.builder().currency("GBP").minorUnits(100).build();
  private final UUID accountUid = randomUUID();
  private final UUID categoryUid = randomUUID();
  private final OffsetDateTime minTransactionTimestamp = now();
  private final OffsetDateTime maxTransactionTimestamp = now();
  private final TransactionFeedItem feedItem = TransactionFeedItem.builder().direction(OUT).amount(amount).build();
  private final TransactionFeedItems feedItems = TransactionFeedItems.builder().feedItems(List.of(feedItem)).build();
  @Autowired
  private TransactionFeedClient transactionFeedClient;
  @Autowired
  private MockRestServiceServer mockRestServiceServer;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("should call transaction feed correctly")
  void request() throws JsonProcessingException {
    String feedItemsString = objectMapper.writeValueAsString(feedItems);
    String transactionFeedUri = format(
        "http://localhost/api/v2/feed/account/%s/category/%s/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s",
        accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);
    mockRestServiceServer.expect(requestTo(transactionFeedUri))
        .andExpect(method(GET))
        .andExpect(header(ACCEPT, APPLICATION_JSON_VALUE))
        .andExpect(header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        .andExpect(header(AUTHORIZATION, "Bearer mock_token"))
        .andRespond(withSuccess(feedItemsString, APPLICATION_JSON));

    transactionFeedClient.transactionFeed(accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);
  }

  @Test
  @DisplayName("should return feed items")
  void response() throws JsonProcessingException {
    String feedItemsString = objectMapper.writeValueAsString(feedItems);
    mockRestServiceServer.expect(requestTo(any(String.class)))
        .andRespond(withSuccess(feedItemsString, APPLICATION_JSON));

    TransactionFeedItems response = transactionFeedClient
        .transactionFeed(accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp);

    assertThat(response).isEqualTo(feedItems);
  }

  @Test
  @DisplayName("should handle 4xx response")
  void badRequest() {
    mockRestServiceServer.expect(requestTo(any(String.class))).andRespond(withBadRequest());

    HttpClientServiceException exception = assertThrows(HttpClientServiceException.class,
        () -> transactionFeedClient
            .transactionFeed(accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp));

    assertThat(exception.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(exception.getMessage()).isEqualTo("Failed to call Transaction Feed API correctly");
  }

  @Test
  @DisplayName("should handle 5xx response")
  void serverError() {
    mockRestServiceServer.expect(requestTo(any(String.class))).andRespond(withServerError());

    HttpClientServiceException exception = assertThrows(HttpClientServiceException.class,
        () -> transactionFeedClient
            .transactionFeed(accountUid, categoryUid, minTransactionTimestamp, maxTransactionTimestamp));

    assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_GATEWAY);
    assertThat(exception.getMessage()).isEqualTo("Transaction Feed API failed to fulfill the request");
  }

}
