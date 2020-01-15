package com.starling.exercise.roundup.web;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.ResponseEntity.ok;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoundupController {

  @PutMapping(value = "/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/roundup/transactions-between")
  public ResponseEntity roundUp(
      @PathVariable("accountUid") UUID acountUid,
      @PathVariable("savingsGoalUid") UUID savingsGoalUid,
      @RequestParam("minTransactionTimestamp") @DateTimeFormat(iso = DATE_TIME) OffsetDateTime minTransactionTimestamp,
      @RequestParam("maxTransactionTimestamp") @DateTimeFormat(iso = DATE_TIME) OffsetDateTime maxTransactionTimestamp
  ) {

    return ok().build();
  }
}
