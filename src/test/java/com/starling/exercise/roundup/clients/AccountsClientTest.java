package com.starling.exercise.roundup.clients;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
import com.starling.exercise.roundup.clients.model.Accounts;
import com.starling.exercise.roundup.clients.model.Accounts.Account;
import com.starling.exercise.roundup.exception.HttpClientServiceException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;

@ExtendWith(SpringExtension.class)
@RestClientTest(AccountsClient.class)
class AccountsClientTest {

  private final Account account = Account.builder().defaultCategory(randomUUID()).build();
  private final Accounts accounts = Accounts.builder().accounts(List.of(account)).build();
  @Autowired
  private AccountsClient accountsClient;
  @Autowired
  private MockRestServiceServer mockRestServiceServer;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("should call accounts correctly")
  void request() throws JsonProcessingException {
    String accountsString = objectMapper.writeValueAsString(accounts);
    String accountsUri = "http://localhost/api/v2/accounts";
    mockRestServiceServer.expect(requestTo(accountsUri))
        .andExpect(method(GET))
        .andExpect(header(ACCEPT, APPLICATION_JSON_VALUE))
        .andExpect(header(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        .andExpect(header(AUTHORIZATION, "Bearer mock_token"))
        .andRespond(withSuccess(accountsString, APPLICATION_JSON));

    accountsClient.accounts();
  }

  @Test
  @DisplayName("should return accounts")
  void response() throws JsonProcessingException {
    String accountsString = objectMapper.writeValueAsString(accounts);
    mockRestServiceServer.expect(requestTo(any(String.class)))
        .andRespond(withSuccess(accountsString, APPLICATION_JSON));

    Accounts response = accountsClient.accounts();

    assertThat(response).isEqualTo(accounts);
  }

  @Test
  @DisplayName("should handle 4xx response")
  void badRequest() {
    mockRestServiceServer.expect(requestTo(any(String.class))).andRespond(withBadRequest());

    HttpClientServiceException exception = assertThrows(HttpClientServiceException.class,
        () -> accountsClient.accounts());

    assertThat(exception.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
    assertThat(exception.getMessage()).isEqualTo("Failed to call Accounts API correctly");
  }

  @Test
  @DisplayName("should handle 5xx response")
  void serverError() {
    mockRestServiceServer.expect(requestTo(any(String.class))).andRespond(withServerError());

    HttpClientServiceException exception = assertThrows(HttpClientServiceException.class,
        () -> accountsClient.accounts());

    assertThat(exception.getStatus()).isEqualTo(BAD_GATEWAY);
    assertThat(exception.getMessage()).isEqualTo("Accounts API failed to fulfill the request");
  }

}
