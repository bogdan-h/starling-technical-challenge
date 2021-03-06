package com.starling.exercise.roundup.web;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.ResponseEntity.ok;

import com.starling.exercise.roundup.service.RoundupService;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoundupController {

  private final RoundupService roundupService;

  @PutMapping("/api/v2/account/{accountUid}/savings-goals/{savingsGoalUid}/roundup/transactions-between")
  public ResponseEntity<StarlingOperation> roundUp(
      @PathVariable("accountUid") UUID accountUid,
      @PathVariable("savingsGoalUid") UUID savingsGoalUid,
      @RequestParam("minTransactionTimestamp") @DateTimeFormat(iso = DATE_TIME) OffsetDateTime minTransactionTimestamp,
      @RequestParam("maxTransactionTimestamp") @DateTimeFormat(iso = DATE_TIME) OffsetDateTime maxTransactionTimestamp
  ) {

    final StarlingOperation starlingOperation = roundupService
        .roundup(accountUid, savingsGoalUid, minTransactionTimestamp, maxTransactionTimestamp);

    return ok().contentType(APPLICATION_JSON).body(starlingOperation);
  }
}
