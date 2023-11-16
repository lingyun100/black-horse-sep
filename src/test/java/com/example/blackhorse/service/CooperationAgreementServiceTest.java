package com.example.blackhorse.service;

import static com.example.blackhorse.TestConstants.*;
import static com.example.blackhorse.constant.Constants.NOT_FOUND;
import static com.example.blackhorse.constant.Constants.SUCCESS;
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
import org.springframework.web.client.RestClientException;

@ExtendWith(MockitoExtension.class)
class CooperationAgreementServiceTest {
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
  void should_return_status_NULL_when_confirmPayFee_given_NULL_data_in_DB() {
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(
            Optional.of(
                CooperationAgreementEntity.builder()
                    .id(COOPERATION_AGREEMENT_ID)
                    .payFeeEntity(
                        PayFeeEntity.builder()
                            .id(ORDER_ID)
                            .status(PayFeeEntity.Status.NULL)
                            .build())
                    .build()));

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

  @Test
  void should_return_status_NULL_when_confirmPayFee_given_null_status_after_save() {
    CooperationAgreementEntity entity =
        CooperationAgreementEntity.builder()
            .id(COOPERATION_AGREEMENT_ID)
            .payFeeEntity(
                PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.PENDING).build())
            .build();
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(Optional.of(entity));
    when(unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID))
        .thenReturn(QueryTransactionResponse.builder().status(NOT_FOUND).build());
    when(cooperationAgreementRepository.save(any()))
        .thenReturn(
            CooperationAgreementEntity.builder()
                .id(COOPERATION_AGREEMENT_ID)
                .payFeeEntity(
                    PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.NULL).build())
                .build());

    PayFeeStatus payFeeStatus =
        cooperationAgreementService.confirmPayFee(COOPERATION_AGREEMENT_ID, SERIAL_ID);

    assertThat(payFeeStatus).isEqualTo(PayFeeStatus.NULL);
  }

  @Test
  void should_return_status_PENDING_when_confirmPayFee_given_TimeoutException_queryTransaction() {
    CooperationAgreementEntity entity =
        CooperationAgreementEntity.builder()
            .id(COOPERATION_AGREEMENT_ID)
            .payFeeEntity(
                PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.PENDING).build())
            .build();
    when(cooperationAgreementRepository.findById(COOPERATION_AGREEMENT_ID))
        .thenReturn(Optional.of(entity));
    when(unionPayClient.queryTransaction(SERIAL_ID, ORDER_ID))
        .thenThrow(new RestClientException("timeout"));
    when(cooperationAgreementRepository.save(any()))
        .thenReturn(
            CooperationAgreementEntity.builder()
                .id(COOPERATION_AGREEMENT_ID)
                .payFeeEntity(
                    PayFeeEntity.builder().id(ORDER_ID).status(PayFeeEntity.Status.PENDING).build())
                .build());

    PayFeeStatus payFeeStatus =
        cooperationAgreementService.confirmPayFee(COOPERATION_AGREEMENT_ID, SERIAL_ID);

    assertThat(payFeeStatus).isEqualTo(PayFeeStatus.PENDING);
  }
}
