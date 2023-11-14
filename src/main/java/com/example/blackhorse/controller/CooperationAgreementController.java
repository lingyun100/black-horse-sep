package com.example.blackhorse.controller;

import static com.example.blackhorse.constant.ApiConstant.*;

import com.example.blackhorse.controller.request.PayFeeConfirmationRequest;
import com.example.blackhorse.controller.response.PayFeeConfirmationResponse;
import com.example.blackhorse.model.PayFeeStatus;
import com.example.blackhorse.service.CooperationAgreementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author linyun.xie
 */
@RestController
@RequestMapping(COOPERATION_AGREEMENT_BASE)
@RequiredArgsConstructor
@Slf4j
public class CooperationAgreementController {
  private final CooperationAgreementService cooperationAgreementService;

  @PostMapping(path = COOPERATION_AGREEMENT_ID + PAY_FEE_CONFIRMATION)
  public PayFeeConfirmationResponse confirmPayFee(
      @PathVariable(value = "cid") String cid, @RequestBody PayFeeConfirmationRequest request) {
    String serialId = request.serialId();
    log.info("start confirm pay fee, cid {}, serialId{}", cid, serialId);
    PayFeeStatus payFeeStatus = cooperationAgreementService.confirmPayFee(cid, serialId);
    return new PayFeeConfirmationResponse(payFeeStatus.name());
  }
}