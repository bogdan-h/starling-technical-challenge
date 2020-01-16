package com.starling.exercise.roundup.steps;

import static com.starling.exercise.roundup.utils.AcceptanceTestContext.objectMapper;
import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;

import com.starling.exercise.roundup.SpringTest;
import com.starling.exercise.roundup.utils.AcceptanceTestContext;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringTest
public class CommonStepDefs {

  private static final String ROUNDUP_URL = "http://localhost:%d/api/v2/account/%s/savings-goals/%s/roundup/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s";

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private AcceptanceTestContext acceptanceTestContext;

  @LocalServerPort
  private int port;

  @When("I invoke the roundup feature on transactions between {string} and {string}")
  public void roundup(String minTransactionTimestamp, String maxTransactionTimestamp) {
    final UUID accountUid = randomUUID();
    acceptanceTestContext.setAccountUid(accountUid);
    final UUID savingsGoalUid = randomUUID();
    acceptanceTestContext.setSavingsGoalUid(savingsGoalUid);
    final String roundupUrl = format(ROUNDUP_URL, port, accountUid, savingsGoalUid, minTransactionTimestamp,
        maxTransactionTimestamp);
    ResponseEntity<String> response = restTemplate.exchange(roundupUrl, PUT, null, String.class, emptyMap());
    acceptanceTestContext.setResponse(response);
  }

  @Then("The HTTP response status will be {int}")
  public void theHttpResponseStatusWillBe(Integer status) {
    Integer actualStatus = acceptanceTestContext.getResponseStatusCode();
    assertThat(actualStatus).isEqualTo(status);
  }

  @Then("The HTTP error message will be {string}")
  public void theHttpErrorMessageWillBe(String message) throws IOException {
    String body = acceptanceTestContext.getResponseBody();
    StarlingOperation response = objectMapper.readValue(body, StarlingOperation.class);
    assertThat(response.getErrors().get(0).getMessage()).isEqualTo(message);
    assertThat(response.getSuccess()).isFalse();
  }
}
