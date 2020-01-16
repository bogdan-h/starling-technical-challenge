package com.starling.exercise.roundup.clients;

import static java.util.UUID.randomUUID;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;

import com.starling.exercise.roundup.clients.model.Amount;
import com.starling.exercise.roundup.clients.model.SavingsGoalTransfer;
import com.starling.exercise.roundup.exception.HttpClientServiceException;
import com.starling.exercise.roundup.web.model.StarlingOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SavingsGoalClient {

  private final RestTemplate restTemplate;
  @Value("${savings-goal.add-money.url}")
  private String savingsGoalUrl;

  public StarlingOperation addMoney(UUID accountUid, UUID savingsGoalUid, Amount amount) {

    HttpHeaders headers = new HttpHeaders();
    headers.add("Accept", "application/json");
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "Bearer mock_token");

    final SavingsGoalTransfer transferRequest = SavingsGoalTransfer.builder().amount(amount).build();

    HttpEntity<SavingsGoalTransfer> httpEntity = new HttpEntity<>(transferRequest, headers);

    Map<String, String> urlParams = new HashMap<>();
    urlParams.put("accountUid", accountUid.toString());
    urlParams.put("savingsGoalUid", savingsGoalUid.toString());
    urlParams.put("transferUid", randomUUID().toString());

    final String url = fromHttpUrl(savingsGoalUrl).buildAndExpand(urlParams).toUriString();

    try {
      ResponseEntity<StarlingOperation> response = restTemplate
          .exchange(url, PUT, httpEntity, StarlingOperation.class);

      return response.getBody();
    } catch (HttpClientErrorException ex) {
      throw new HttpClientServiceException("Failed to call Savings Goal API correctly", INTERNAL_SERVER_ERROR);
    } catch (HttpServerErrorException ex) {
      throw new HttpClientServiceException("Savings Goal API failed to fulfill the request", BAD_GATEWAY);
    } catch (Exception e) {
      throw new HttpClientServiceException("Something unrecoverable has happened", INTERNAL_SERVER_ERROR);
    }
  }
}
