package com.starling.exercise.roundup.web;

import static java.lang.String.format;
import static java.time.OffsetDateTime.now;
import static java.util.Collections.emptyList;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starling.exercise.roundup.service.RoundupService;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountsControllerTest {

  private static final String ROUNDUP_URL = "/api/v2/account/%s/savings-goals/%s/roundup/transactions-between?minTransactionTimestamp=%s&maxTransactionTimestamp=%s";
  private final UUID accountUid = randomUUID();
  private final UUID savingsGoalUid = randomUUID();
  private final OffsetDateTime minTransactionTimestamp = now();
  private final OffsetDateTime maxTransactionTimestamp = now();
  @MockBean
  private RoundupService roundupService;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @DisplayName("should handle request")
  void roundupRequest() throws Exception {
    StarlingOperation starlingOperation = StarlingOperation.builder().success(true).errors(emptyList()).build();
    when(roundupService.roundup(accountUid, savingsGoalUid, minTransactionTimestamp, maxTransactionTimestamp))
        .thenReturn(starlingOperation);

    String roundupUrl = format(ROUNDUP_URL, accountUid, savingsGoalUid, minTransactionTimestamp,
        maxTransactionTimestamp);

    mockMvc.perform(put(roundupUrl))
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().string(objectMapper.writeValueAsString(starlingOperation)));
  }
}
