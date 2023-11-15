package com.example.blackhorse.infra.rpc.request;

import lombok.*;

/**
 * @author linyun.xie
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryTransactionRequest {
  private String serialId;
  private String orderId;
}
