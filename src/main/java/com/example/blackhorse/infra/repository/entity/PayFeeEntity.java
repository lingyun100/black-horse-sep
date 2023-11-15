package com.example.blackhorse.infra.repository.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author linyun.xie
 */
@Entity
@Table(name = "pay_fee")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PayFeeEntity {
  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private Status status;

  public enum Status {
    /** */
    NULL,
    /** */
    PENDING,
    /** */
    CONFIRMED
  }
}
