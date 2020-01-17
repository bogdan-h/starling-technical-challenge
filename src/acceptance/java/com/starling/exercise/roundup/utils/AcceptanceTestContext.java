package com.starling.exercise.roundup.utils;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

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
