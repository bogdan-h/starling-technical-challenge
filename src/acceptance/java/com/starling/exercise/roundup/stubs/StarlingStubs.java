package com.starling.exercise.roundup.stubs;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.starling.exercise.roundup.utils.ResponseHolder.objectMapper;
import static java.util.UUID.randomUUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.starling.exercise.roundup.clients.model.Accounts;
import com.starling.exercise.roundup.clients.model.Accounts.Account;
import java.util.List;

public class StarlingStubs {

  private static final String ACCOUNTS_PATH = "/api/v2/accounts";

  public static void verifyAccounts() {
    verify(getRequestedFor(urlEqualTo(ACCOUNTS_PATH))
        .withHeader("Accept", equalTo("application/json"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withHeader("Authorization", equalTo("Bearer mock_token")));
  }

  public static void stubAccounts(Integer status) throws JsonProcessingException {
    Accounts accounts = Accounts.builder().accounts(List.of(Account.builder().defaultCategory(randomUUID()).build()))
        .build();

    stubFor(get(ACCOUNTS_PATH)
        .willReturn(aResponse().withHeader("Content-Type", "application/json")
            .withBody(objectMapper.writeValueAsString(accounts)).withStatus(status)));
  }
}
