package com.starling.exercise.roundup.utils;

import com.starling.exercise.roundup.steps.CommonStepDefs;
import com.starling.exercise.roundup.stubs.StarlingStubs;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * This class holds the response from the executed REST resource as it is needed in different parts of the test.
 *
 * {@code response} is created in {@link CommonStepDefs#roundup} but is asserted on in {@link
 * CommonStepDefs#theHttpResponseStatusWillBe} and {@link CommonStepDefs#theHttpErrorMessageWillBe}. As the system
 * evolves, the response will be needed in increasingly more classes so it is good practice to hold it in a class.
 *
 *
 * This class also holds auto-generated IDs that are needed in different parts of the test.
 *
 * For example, {@code accountUid} is auto-generated in {@link CommonStepDefs#roundup} but is asserted on in {@link
 * StarlingStubs#verifyTransactionFeed} ad {@link StarlingStubs#verifySavingsGoal}
 */
@Component
public class AcceptanceTestContext {

  private ResponseEntity<String> response;
  private UUID accountUid;
  private UUID defaultCategoryUid;
  private UUID savingsGoalUid;

  public void setResponse(ResponseEntity<String> response) {
    this.response = response;
  }

  public Integer getResponseStatusCode() {
    return response.getStatusCodeValue();
  }

  public List<String> getResponseHeader(String headerKey) {
    return response.getHeaders().get(headerKey);
  }

  public String getResponseBody() {
    return response.getBody();
  }

  public UUID getAccountUid() {
    return accountUid;
  }

  public void setAccountUid(UUID accountUid) {
    this.accountUid = accountUid;
  }

  public UUID getDefaultCategoryUid() {
    return defaultCategoryUid;
  }

  public void setDefaultCategoryUid(UUID defaultCategoryUid) {
    this.defaultCategoryUid = defaultCategoryUid;
  }

  public UUID getSavingsGoalUid() {
    return savingsGoalUid;
  }

  public void setSavingsGoalUid(UUID savingsGoalUid) {
    this.savingsGoalUid = savingsGoalUid;
  }
}
