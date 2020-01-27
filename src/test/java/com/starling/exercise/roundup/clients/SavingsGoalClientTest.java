package com.starling.exercise.roundup.clients;

import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.SavingsGoalTransfer;
import com.starling.exercise.roundup.exception.HttpClientServiceException;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

@ExtendWith(SpringExtension.class)
@RestClientTest(SavingsGoalClient.class)
class SavingsGoalClientTest {

  private final Amount amount = Amount.builder().currency("GBP").minorUnits(100).build();
  private final UUID accountUid = randomUUID();
  private final UUID savingsGoalUid = randomUUID();
  private final StarlingOperation starlingOperation = StarlingOperation.builder().success(true).build();
  @Autowired
  private SavingsGoalClient savingsGoalClient;
  @Autowired
  private MockRestServiceServer mockRestServiceServer;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("should call savings goal correctly")
  void request() throws JsonProcessingException {
    String starlingOperationString = objectMapper.writeValueAsString(starlingOperation);
    SavingsGoalTransfer savingsGoalTransfer = SavingsGoalTransfer.builder().amount(amount).build();
    String savingsGoalTransferString = objectMapper.writeValueAsString(savingsGoalTransfer);
    String savingsGoalUri = format("http://localhost/api/v2/account/%s/savings-goals/%s/add-money",
        accountUid, savingsGoalUid);
    mockRestServiceServer.expect(requestTo(containsString(savingsGoalUri)))
        .andExpect(method(PUT))
        .andExpect(content().string(savingsGoalTransferString))
        .andExpect(header(ACCEPT, APPLICATION_JSON_VALUE))
        .andExpect(header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        .andExpect(header(AUTHORIZATION, "Bearer mock_token"))
        .andRespond(withSuccess(starlingOperationString, APPLICATION_JSON));

    savingsGoalClient.addMoney(accountUid, savingsGoalUid, amount);
  }

  @Test
  @DisplayName("should return starling operation")
  void response() throws JsonProcessingException {
    String starlingOperationString = objectMapper.writeValueAsString(starlingOperation);
    mockRestServiceServer.expect(requestTo(any(String.class)))
        .andRespond(withSuccess(starlingOperationString, APPLICATION_JSON));

    StarlingOperation response = savingsGoalClient.addMoney(accountUid, savingsGoalUid, amount);

    assertThat(response).isEqualTo(starlingOperation);
  }

  @Test
  @DisplayName("should handle 4xx response")
  void badRequest() {
    mockRestServiceServer.expect(requestTo(any(String.class))).andRespond(withBadRequest());

    HttpClientServiceException exception = assertThrows(HttpClientServiceException.class,
        () -> savingsGoalClient.addMoney(accountUid, savingsGoalUid, amount));

    assertThat(exception.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(exception.getMessage()).isEqualTo("Failed to call Savings Goal API correctly");
  }

  @Test
  @DisplayName("should handle 5xx response")
  void serverError() {
    mockRestServiceServer.expect(requestTo(any(String.class))).andRespond(withServerError());

    HttpClientServiceException exception = assertThrows(HttpClientServiceException.class,
        () -> savingsGoalClient.addMoney(accountUid, savingsGoalUid, amount));

    assertThat(exception.getStatus()).isEqualTo(BAD_GATEWAY);
    assertThat(exception.getMessage()).isEqualTo("Savings Goal API failed to fulfill the request");
  }

}
