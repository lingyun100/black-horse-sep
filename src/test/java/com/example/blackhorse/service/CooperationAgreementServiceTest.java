package com.example.blackhorse.service;

import static com.example.blackhorse.service.CooperationAgreementService.SUCCESS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.blackhorse.infra.repository.CooperationAgreementRepository;
import com.example.blackhorse.infra.repository.entity.CooperationAgreementEntity;
import com.example.blackhorse.infra.repository.entity.PayFeeEntity;
import com.example.blackhorse.infra.rpc.UnionPayClient;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import com.example.blackhorse.model.PayFeeStatus;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CooperationAgreementServiceTest {
  private static final String COOPERATION_AGREEMENT_ID = "CA001";
  private static final String ORDER_ID = "O0001";
  private static final String SERIAL_ID = "S0001";

  @Mock private UnionPayClient unionPayClient;
  @Mock private CooperationAgreementRepository cooperationAgreementRepository;
  @InjectMocks private CooperationAgreementService cooperationAgreementService;

  @Test
  void should_return_status_NULL_when_confirmPayFee_given_empty_data_in_DB() {
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(Optional.empty());

    PayFeeStatus payFeeStatus =
        cooperationAgreementService.confirmPayFee(COOPERATION_AGREEMENT_ID, SERIAL_ID);

    assertThat(payFeeStatus).isEqualTo(PayFeeStatus.NULL);
  }

  @Test
  void should_return_status_PENDING_when_confirmPayFee_given_null_from_query_transaction() {
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(
            Optional.of(
                CooperationAgreementEntity.builder()
                    .id(COOPERATION_AGREEMENT_ID)
                    .payFeeEntity(
                        PayFeeEntity.builder()
                            .id(ORDER_ID)
                            .status(PayFeeEntity.Status.PENDING)
                            .build())
                    .build()));
    when(unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID)).thenReturn(null);

    PayFeeStatus payFeeStatus =
        cooperationAgreementService.confirmPayFee(COOPERATION_AGREEMENT_ID, SERIAL_ID);

    assertThat(payFeeStatus).isEqualTo(PayFeeStatus.PENDING);
  }

  @Test
  void should_return_status_PENDING_when_confirmPayFee_given_not_SUCCESS_from_query_transaction() {
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(
            Optional.of(
                CooperationAgreementEntity.builder()
                    .id(COOPERATION_AGREEMENT_ID)
                    .payFeeEntity(
                        PayFeeEntity.builder()
                            .id(ORDER_ID)
                            .status(PayFeeEntity.Status.PENDING)
                            .build())
                    .build()));
    when(unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID))
        .thenReturn(QueryTransactionResponse.builder().build());

    PayFeeStatus payFeeStatus =
        cooperationAgreementService.confirmPayFee(COOPERATION_AGREEMENT_ID, SERIAL_ID);

    assertThat(payFeeStatus).isEqualTo(PayFeeStatus.PENDING);
  }

  @Test
  void should_return_status_CONFIRMED_when_confirmPayFee_given_confirmed_status_after_save() {
    CooperationAgreementEntity entity =
        CooperationAgreementEntity.builder()
            .id(COOPERATION_AGREEMENT_ID)
            .payFeeEntity(
                PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.PENDING).build())
            .build();
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(Optional.of(entity));
    when(unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID))
        .thenReturn(QueryTransactionResponse.builder().status(SUCCESS).build());

    when(cooperationAgreementRepository.save(any()))
        .thenReturn(
            CooperationAgreementEntity.builder()
                .id(COOPERATION_AGREEMENT_ID)
                .payFeeEntity(
                    PayFeeEntity.builder()
                        .id(ORDER_ID)
                        .status(PayFeeEntity.Status.CONFIRMED)
                        .build())
                .build());

    PayFeeStatus payFeeStatus =
        cooperationAgreementService.confirmPayFee(COOPERATION_AGREEMENT_ID, SERIAL_ID);

    assertThat(payFeeStatus).isEqualTo(PayFeeStatus.CONFIRMED);
  }
}
