package com.example.blackhorse.service;

import static com.example.blackhorse.constant.Constants.NOT_FOUND;
import static com.example.blackhorse.constant.Constants.SUCCESS;
import static com.example.blackhorse.infra.repository.entity.PayFeeEntity.Status.CONFIRMED;
import static com.example.blackhorse.infra.repository.entity.PayFeeEntity.Status.NULL;

import com.example.blackhorse.infra.repository.CooperationAgreementRepository;
import com.example.blackhorse.infra.repository.entity.CooperationAgreementEntity;
import com.example.blackhorse.infra.repository.entity.PayFeeEntity;
import com.example.blackhorse.infra.rpc.UnionPayClient;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import com.example.blackhorse.model.PayFeeStatus;

import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author linyun.xie
 */
@Service
@RequiredArgsConstructor
public class CooperationAgreementService {

  private final CooperationAgreementRepository cooperationAgreementRepository;

  private final UnionPayClient unionPayClient;

  public PayFeeStatus confirmPayFee(String cid, String serialId) {
    Optional<CooperationAgreementEntity> agreementEntityOpt = cooperationAgreementRepository.findById(cid);
    if (agreementEntityOpt.isEmpty()) {
      return payFeeStatus(NULL);
    }

    CooperationAgreementEntity cooperationAgreementEntity = agreementEntityOpt.get();

    if(NULL == cooperationAgreementEntity.getPayFeeEntity().getStatus()){
      return payFeeStatus(NULL);
    }

    QueryTransactionResponse transactionResponse =
        unionPayClient.queryTransaction(
            serialId, cooperationAgreementEntity.getPayFeeEntity().getId());

    if( transactionResponse == null ){
      return PayFeeStatus.PENDING;
    }

    if (!Arrays.asList(SUCCESS,NOT_FOUND).contains(transactionResponse.getStatus())) {
      return PayFeeStatus.PENDING;
    }

    cooperationAgreementEntity.getPayFeeEntity().setStatus(getStatusFromTransactionStatus(transactionResponse.getStatus()));

    return payFeeStatus(cooperationAgreementRepository.save(cooperationAgreementEntity).getPayFeeEntity().getStatus());
  }

  private PayFeeStatus payFeeStatus(PayFeeEntity.Status status){
    return switch (status){
      case PENDING ->  PayFeeStatus.PENDING;
      case CONFIRMED -> PayFeeStatus.CONFIRMED;
      case NULL -> PayFeeStatus.NULL;
    };
  }

  private PayFeeEntity.Status getStatusFromTransactionStatus(String transactionStatus){
    return switch (transactionStatus){
      case SUCCESS ->  CONFIRMED;
      case NOT_FOUND -> NULL;
      default -> throw new RuntimeException("invalid");
    };
  }
}
