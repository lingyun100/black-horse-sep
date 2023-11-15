package com.example.blackhorse.infra.rpc.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author linyun.xie
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryTransactionResponse {
  private String status;
}
