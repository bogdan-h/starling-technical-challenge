package com.starling.exercise.roundup.steps;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.starling.exercise.roundup.stubs.StarlingStubs;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class StubStepDefs {

  @Autowired
  private StarlingStubs starlingStubs;

  @Given("The Accounts API responds with {int}")
  public void stubAccounts(Integer status) throws JsonProcessingException {
    starlingStubs.stubAccounts(status);
  }

  @Given("The Transaction Feed API responds with {int}")
  public void stubTransactionFeed(Integer status) throws JsonProcessingException {
    starlingStubs.stubTransactionFeed(status);
  }

  @Given("The Transaction Feed API responds with the following feed items {string}")
  public void stubTransactionFeedWithFeedItems(String amountValues) throws JsonProcessingException {
    final List<Integer> amounts = stream(amountValues.split(",")).mapToInt(Integer::parseInt).boxed().collect(toList());
    starlingStubs.stubTransactionFeed(amounts);
  }

  @Given("The Savings Goal API responds with {int}")
  public void stubSavingsGoal(Integer status) throws JsonProcessingException {
    starlingStubs.stubSavingsGoal(status);
  }

  @Given("The Savings Goal API responds with error message {string}")
  public void stubSavingsGoalWithFailure(String message) throws JsonProcessingException {
    starlingStubs.stubSavingsGoalWithFailure(message);
  }

  @Then("The Accounts API has been called correctly")
  public void verifyAccounts() {
    starlingStubs.verifyAccounts();
  }

  @Then("The Transaction Feed API has been called correctly for transactions between {string} and {string}")
  public void verifyTransactionFeed(String minTransactionTimestamp, String maxTransactionTimestamp) {
    starlingStubs.verifyTransactionFeed(minTransactionTimestamp, maxTransactionTimestamp);
  }

  @Then("The Savings Goal API has been called correctly")
  public void verifySavingsGoal() {
    starlingStubs.verifySavingsGoal();
  }

  @Then("The Savings Goal API has been called correctly with {int}")
  public void verifySavingsGoalWithAmount(Integer amount) throws JsonProcessingException {
    starlingStubs.verifySavingsGoal(amount);
  }
}
