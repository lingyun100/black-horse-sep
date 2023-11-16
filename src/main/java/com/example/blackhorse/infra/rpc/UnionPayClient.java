package com.example.blackhorse.infra.rpc;

import static com.example.blackhorse.constant.ApiConstant.QUERY_TRANSACTION_API;

import com.example.blackhorse.infra.rpc.request.QueryTransactionRequest;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author linyun.xie
 */
@Component
@RequiredArgsConstructor
public class UnionPayClient {

  private final RestTemplateBuilder restTemplateBuilder;

  private RestTemplate restTemplate;

  @Value("${union_pay.base.path}")
  private String basePath;

  @PostConstruct
  private void initRestTemplate() {
    restTemplate =
        restTemplateBuilder
            .setReadTimeout(Duration.ofMillis(100))
            .setConnectTimeout(Duration.ofSeconds(3))
            .build();
  }

  @Retryable(retryFor = Exception.class, backoff = @Backoff(multiplier = 2))
  public QueryTransactionResponse queryTransaction(String serialId, String orderId) {
    ResponseEntity<QueryTransactionResponse> response =
        restTemplate.postForEntity(
            basePath + QUERY_TRANSACTION_API,
            QueryTransactionRequest.builder().serialId(serialId).orderId(orderId).build(),
            QueryTransactionResponse.class);
    return response.getBody();
  }
}
