package com.starling.exercise.roundup.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.starling.exercise.roundup.stubs.StarlingStubs;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

public class StubStepDefs {

  @Autowired
  private StarlingStubs starlingStubs;

  @Given("^The Accounts API responds with (.*)$")
  public void stubAccounts(Integer status) throws JsonProcessingException {
    starlingStubs.stubAccounts(status);
  }

  @Given("^The Transaction Feed API responds with (.*)$")
  public void stubTransactionFeed(Integer status) throws JsonProcessingException {
    starlingStubs.stubTransactionFeed(status);
  }

  @Then("The Accounts API has been called correctly")
  public void verifyAccounts() {
    starlingStubs.verifyAccounts();
  }

  @Then("The Transaction Feed API has been called correctly for transactions between {string} and {string}")
  public void verifyTransactionFeed(String minTransactionTimestamp, String maxTransactionTimestamp) {
    starlingStubs.verifyTransactionFeed(minTransactionTimestamp, maxTransactionTimestamp);
  }
}
