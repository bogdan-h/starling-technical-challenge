package com.starling.exercise.roundup.steps;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.PUT;

import com.starling.exercise.roundup.SpringTest;
import com.starling.exercise.roundup.utils.ResponseHolder;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
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
  private ResponseHolder responseHolder;

  @LocalServerPort
  private int port;

  @When("I invoke the roundup feature on transactions between {string} and {string}")
  public void roundup(String from, String to) {
    final String roundupUrl = format(ROUNDUP_URL, port, randomUUID(), randomUUID(), from, to);
    ResponseEntity<String> response = restTemplate.exchange(roundupUrl, PUT, null, String.class, emptyMap());
    responseHolder.set(response);
  }

  @Then("^The HTTP response status will be (.*)$")
  public void theHttpResponseStatusWillBe(Integer expectedStatus) {
    Integer actualStatus = responseHolder.geStatusCodeValue();
    assertThat(actualStatus).isEqualTo(expectedStatus);
  }
}
