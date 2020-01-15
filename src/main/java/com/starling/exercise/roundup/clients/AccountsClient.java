package com.starling.exercise.roundup.clients;

import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.starling.exercise.roundup.clients.model.Accounts;
import com.starling.exercise.roundup.exception.HttpClientServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AccountsClient {

  private final RestTemplate restTemplate;
  @Value("${accounts.url}")
  private String accountsUrl;

  public Accounts accounts() {

    final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.add("Accept", "application/json");
    headers.add("Content-Type", "application/json");
    headers.add("Authorization", "Bearer mock_token");
    HttpEntity httpEntity = new HttpEntity<>(null, headers);

    try {
      ResponseEntity<Accounts> response = restTemplate
          .exchange(accountsUrl, GET, httpEntity, Accounts.class, emptyMap());

      return response.getBody();
    } catch (HttpClientErrorException ex) {
      throw new HttpClientServiceException("Failed to call accounts API correctly", INTERNAL_SERVER_ERROR);
    } catch (HttpServerErrorException ex) {
      throw new HttpClientServiceException("Accounts API failed to fulfill the request", BAD_GATEWAY);
    } catch (Exception e) {
      throw new HttpClientServiceException("Something unrecoverable has happened", INTERNAL_SERVER_ERROR);
    }
  }
}
