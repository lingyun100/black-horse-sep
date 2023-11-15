package com.example.blackhorse.infra.repository.entity;

import static jakarta.persistence.CascadeType.ALL;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author linyun.xie
 */
@Entity
@Table(name = "cooperation_agreement")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CooperationAgreementEntity {
  @Id
  @Column(name = "id")
  private String id;

  @OneToOne(cascade = ALL)
  @JoinColumn(name = "pay_fee_id", referencedColumnName = "id")
  private PayFeeEntity payFeeEntity;
}
