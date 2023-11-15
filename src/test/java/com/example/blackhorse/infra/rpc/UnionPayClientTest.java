package com.example.blackhorse.infra.rpc;

import static com.example.blackhorse.TestConstants.ORDER_ID;
import static com.example.blackhorse.TestConstants.SERIAL_ID;
import static com.example.blackhorse.constant.ApiConstant.QUERY_TRANSACTION_API;
import static com.example.blackhorse.constant.Constants.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.POST;

import com.example.blackhorse.BaseIntegrationTest;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

class UnionPayClientTest extends BaseIntegrationTest {
  @Autowired UnionPayClient unionPayClient;

  @Autowired ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void should_return_SUCCESS_when_query_transaction_given_SUCCESS_from_fake_union_pay_platform() {
    mockServerClient
        .when(request().withMethod(POST.name()).withPath(QUERY_TRANSACTION_API), unlimited())
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    objectMapper.writeValueAsString(
                        QueryTransactionResponse.builder().status(SUCCESS).build()))
                .withDelay(TimeUnit.MILLISECONDS, 1000));

    QueryTransactionResponse queryTransactionResponse =
        unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID);
    assertThat(queryTransactionResponse)
        .usingRecursiveComparison()
        .isEqualTo(QueryTransactionResponse.builder().status(SUCCESS).build());
  }
}
