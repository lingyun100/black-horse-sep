package com.example.blackhorse.controller;

import static com.example.blackhorse.constant.ApiConstant.COOPERATION_AGREEMENT_BASE;
import static com.example.blackhorse.constant.ApiConstant.PAY_FEE_CONFIRMATION;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.blackhorse.model.PayFeeStatus;
import com.example.blackhorse.service.CooperationAgreementService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CooperationAgreementController.class)
class CooperationAgreementControllerTest {

  @Autowired MockMvc mockMvc;

  @MockBean CooperationAgreementService cooperationAgreementService;

  @SneakyThrows
  @Test
  void should_return_status_CONFIRMED_when_confirm_pay_fee_given_CONFIRMED_from_service() {
    when(cooperationAgreementService.confirmPayFee("CA001", "S0001"))
        .thenReturn(PayFeeStatus.CONFIRMED);

    mockMvc
        .perform(
            post(COOPERATION_AGREEMENT_BASE + "/" + "CA001" + PAY_FEE_CONFIRMATION)
                .content("{\"serialId\":\"S0001\"}")
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", equalTo("CONFIRMED")));
  }

  @SneakyThrows
  @Test
  void should_return_status_NULL_and_400_when_confirm_pay_fee_given_NULL_from_service() {
    when(cooperationAgreementService.confirmPayFee("CA001", "S0001")).thenReturn(PayFeeStatus.NULL);

    mockMvc
        .perform(
            post(COOPERATION_AGREEMENT_BASE + "/" + "CA001" + PAY_FEE_CONFIRMATION)
                .content("{\"serialId\":\"S0001\"}")
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", equalTo("NULL")));
  }

  @SneakyThrows
  @Test
  void should_return_status_NULL_and_500_when_confirm_pay_fee_given_PENDING_from_service() {
    when(cooperationAgreementService.confirmPayFee("CA001", "S0001"))
        .thenReturn(PayFeeStatus.PENDING);

    mockMvc
        .perform(
            post(COOPERATION_AGREEMENT_BASE + "/" + "CA001" + PAY_FEE_CONFIRMATION)
                .content("{\"serialId\":\"S0001\"}")
                .contentType(APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.status", equalTo("PENDING")));
  }
}
