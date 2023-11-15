package com.example.blackhorse.infra.rpc;

import static com.example.blackhorse.constant.ApiConstant.QUERY_TRANSACTION_API;

import com.example.blackhorse.infra.rpc.request.QueryTransactionRequest;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author linyun.xie
 */
@Component
public class UnionPayClient {
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${union_pay.base.path}")
  private String basePath;

  public QueryTransactionResponse queryTransaction(String serialId, String orderId) {
    ResponseEntity<QueryTransactionResponse> response =
        restTemplate.postForEntity(
            basePath + QUERY_TRANSACTION_API,
            QueryTransactionRequest.builder().serialId(serialId).orderId(orderId).build(),
            QueryTransactionResponse.class);
    return response.getBody();
  }
}
