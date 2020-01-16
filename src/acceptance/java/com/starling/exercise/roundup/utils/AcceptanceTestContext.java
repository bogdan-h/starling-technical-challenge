package com.starling.exercise.roundup.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AcceptanceTestContext {

  public static final ObjectMapper objectMapper = new ObjectMapper();

  private ResponseEntity<String> response;
  private UUID accountUid;
  private UUID defaultCategoryUid;

  public void setResponse(ResponseEntity<String> response) {
    this.response = response;
  }

  public Integer getResponseStatusCode() {
    return response.getStatusCodeValue();
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
}
