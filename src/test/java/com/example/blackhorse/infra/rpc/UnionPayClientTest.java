package com.example.blackhorse.infra.rpc;

import static com.example.blackhorse.TestConstants.ORDER_ID;
import static com.example.blackhorse.TestConstants.SERIAL_ID;
import static com.example.blackhorse.constant.ApiConstant.QUERY_TRANSACTION_API;
import static com.example.blackhorse.constant.Constants.NOT_FOUND;
import static com.example.blackhorse.constant.Constants.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.POST;

import com.example.blackhorse.BaseIntegrationTest;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

class UnionPayClientTest extends BaseIntegrationTest {
  @Autowired UnionPayClient unionPayClient;

  @Autowired ObjectMapper objectMapper;

  @AfterEach
  void tearDown() {
    mockServerClient.reset();
  }

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
                        QueryTransactionResponse.builder().status(SUCCESS).build())));

    QueryTransactionResponse queryTransactionResponse =
        unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID);
    assertThat(queryTransactionResponse)
        .usingRecursiveComparison()
        .isEqualTo(QueryTransactionResponse.builder().status(SUCCESS).build());
  }

  @Test
  @SneakyThrows
  void
      should_return_NOT_FOUND_when_query_transaction_given_NOT_FOUND_from_fake_union_pay_platform() {
    mockServerClient
        .when(request().withMethod(POST.name()).withPath(QUERY_TRANSACTION_API), unlimited())
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    objectMapper.writeValueAsString(
                        QueryTransactionResponse.builder().status(NOT_FOUND).build())));

    QueryTransactionResponse queryTransactionResponse =
        unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID);
    assertThat(queryTransactionResponse)
        .usingRecursiveComparison()
        .isEqualTo(QueryTransactionResponse.builder().status(NOT_FOUND).build());
  }

  @Test
  @SneakyThrows
  void
      should_return_SUCCESS_when_query_transaction_given_first_call_timeout_then_success_from_fake_union_pay_platform() {
    mockServerClient
        .when(request().withMethod(POST.name()).withPath(QUERY_TRANSACTION_API), exactly(1))
        .respond(
            response()
                .withStatusCode(500)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody("")
                .withDelay(TimeUnit.MILLISECONDS, 200));
    mockServerClient
        .when(request().withMethod(POST.name()).withPath(QUERY_TRANSACTION_API), exactly(2))
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(
                    objectMapper.writeValueAsString(
                        QueryTransactionResponse.builder().status(SUCCESS).build())));

    QueryTransactionResponse queryTransactionResponse =
        unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID);
    assertThat(queryTransactionResponse)
        .usingRecursiveComparison()
        .isEqualTo(QueryTransactionResponse.builder().status(SUCCESS).build());
  }
}
