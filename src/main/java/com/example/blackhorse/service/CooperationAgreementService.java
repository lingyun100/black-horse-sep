package com.example.blackhorse.service;

import static com.example.blackhorse.infra.repository.entity.PayFeeEntity.Status.CONFIRMED;

import com.example.blackhorse.infra.repository.CooperationAgreementRepository;
import com.example.blackhorse.infra.repository.entity.CooperationAgreementEntity;
import com.example.blackhorse.infra.repository.entity.PayFeeEntity;
import com.example.blackhorse.infra.rpc.UnionPayClient;
import com.example.blackhorse.infra.rpc.response.QueryTransactionResponse;
import com.example.blackhorse.model.PayFeeStatus;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author linyun.xie
 */
@Service
@RequiredArgsConstructor
public class CooperationAgreementService {

  public static final String SUCCESS = "SUCCESS";

  private final CooperationAgreementRepository cooperationAgreementRepository;

  private final UnionPayClient unionPayClient;

  public PayFeeStatus confirmPayFee(String cid, String serialId) {
    Optional<CooperationAgreementEntity> agreementEntityOpt = cooperationAgreementRepository.findById(cid);
    if (agreementEntityOpt.isEmpty()) {
      return PayFeeStatus.NULL;
    }

    CooperationAgreementEntity cooperationAgreementEntity = agreementEntityOpt.get();

    QueryTransactionResponse transactionResponse =
        unionPayClient.queryTransaction(
            serialId, cooperationAgreementEntity.getPayFeeEntity().getId());

    if (transactionResponse != null && SUCCESS.equals(transactionResponse.getStatus())) {
      cooperationAgreementEntity.getPayFeeEntity().setStatus(CONFIRMED);
      return payFeeStatus(cooperationAgreementRepository.save(cooperationAgreementEntity).getPayFeeEntity().getStatus());
    }

    return PayFeeStatus.PENDING;
  }

  private PayFeeStatus payFeeStatus(PayFeeEntity.Status status){
    return switch (status){
      case PENDING ->  PayFeeStatus.PENDING;
      case CONFIRMED -> PayFeeStatus.CONFIRMED;
      case NULL -> PayFeeStatus.NULL;
    };
  }
}
