package com.example.blackhorse.infra.repository;

import static com.example.blackhorse.TestConstants.COOPERATION_AGREEMENT_ID;
import static com.example.blackhorse.TestConstants.ORDER_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.blackhorse.infra.repository.entity.CooperationAgreementEntity;
import com.example.blackhorse.infra.repository.entity.PayFeeEntity;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CooperationAgreementRepositoryTest {
  @Autowired private CooperationAgreementRepository cooperationAgreementRepository;

  @Test
  void should_get_status_PENDING_when_findById_given_PENDING_data_in_db() {
    prepareData();

    Optional<CooperationAgreementEntity> entityOptional =
        cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID);

    assertThat(entityOptional)
        .get()
        .usingRecursiveComparison()
        .isEqualTo(
            CooperationAgreementEntity.builder()
                .id(COOPERATION_AGREEMENT_ID)
                .payFeeEntity(
                    PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.PENDING).build())
                .build());
  }

  @ParameterizedTest
  @EnumSource(PayFeeEntity.Status.class)
  void should_get_correct_Status_from_db_when_save_to_db_with_given_Status_data(
      PayFeeEntity.Status status) {
    cooperationAgreementRepository.save(
        CooperationAgreementEntity.builder()
            .id(COOPERATION_AGREEMENT_ID)
            .payFeeEntity(PayFeeEntity.builder().id(ORDER_ID).status(status).build())
            .build());

    Optional<CooperationAgreementEntity> entityOptional =
        cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID);
    assertThat(entityOptional)
        .map(CooperationAgreementEntity::getPayFeeEntity)
        .map(PayFeeEntity::getStatus)
        .get()
        .isEqualTo(status);
  }

  void prepareData() {
    cooperationAgreementRepository.save(
        CooperationAgreementEntity.builder()
            .id(COOPERATION_AGREEMENT_ID)
            .payFeeEntity(
                PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.PENDING).build())
            .build());
  }
}
