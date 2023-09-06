package com.example.blackhorsesep.controller;

import static com.example.blackhorsesep.TestData.*;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCES;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCE_BASE;
import static com.example.blackhorsesep.constant.Constant.URL_PATH;
import static com.example.blackhorsesep.exception.ErrorCodeConstant.PARAM_VALIDATE_FAILURE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.blackhorsesep.model.ResourceModel;
import com.example.blackhorsesep.service.ResourceService;
import java.util.List;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ResourceController.class)
class ResourceControllerTest {

  @Autowired MockMvc mockMvc;

  @MockBean ResourceService resourceService;

  @SneakyThrows
  @Test
  void should_return_200_and_8_resources_when_get_resource_given_8_resources_from_service() {
    int resourceNum = 8;
    List<ResourceModel> resourceModelList = initResourceModelList(resourceNum);
    when(resourceService.getResources(eq(DEFAULT_RESOURCE_ID), anyInt(), anyInt()))
        .thenReturn(resourceModelList);

    mockMvc
        .perform(
            get(RESOURCE_BASE + URL_PATH + DEFAULT_RESOURCE_ID + RESOURCES)
                .param(PAGE_NO_KEY, DEFAULT_PAGE_NO_STR)
                .param(PAGE_SIZE_KEY, DEFAULT_PAGE_SIZE_STR)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(resourceNum)));
  }

  @SneakyThrows
  @ParameterizedTest
  @CsvSource({"-1,10", "1,-1", "1,1001"})
  void should_return_400_and_param_validate_failure_when_get_resource_given_params_illegal(
      String pageNo, String pageSize) {
    mockMvc
        .perform(
            get(RESOURCE_BASE + URL_PATH + DEFAULT_RESOURCE_ID + RESOURCES)
                .param(PAGE_NO_KEY, pageNo)
                .param(PAGE_SIZE_KEY, pageSize)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode", equalTo(PARAM_VALIDATE_FAILURE)));
  }

  @SneakyThrows
  @Test
  void should_return_200_and_10_resources_when_get_resource_given_10_resources_from_service() {
    int resourceNum = 10;
    List<ResourceModel> resourceModelList = initResourceModelList(resourceNum);
    when(resourceService.getResources(eq(DEFAULT_RESOURCE_ID), anyInt(), anyInt()))
        .thenReturn(resourceModelList);

    mockMvc
        .perform(
            get(RESOURCE_BASE + URL_PATH + DEFAULT_RESOURCE_ID + RESOURCES)
                .param(PAGE_NO_KEY, DEFAULT_PAGE_NO_STR)
                .param(PAGE_SIZE_KEY, DEFAULT_PAGE_SIZE_STR)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(resourceNum)));
  }

  @SneakyThrows
  @Test
  void should_return_200_and_0_resources_when_get_resource_given_0_resources_from_service() {
    int resourceNum = 0;
    List<ResourceModel> resourceModelList = initResourceModelList(resourceNum);
    when(resourceService.getResources(eq(DEFAULT_RESOURCE_ID), anyInt(), anyInt()))
        .thenReturn(resourceModelList);

    mockMvc
        .perform(
            get(RESOURCE_BASE + URL_PATH + DEFAULT_RESOURCE_ID + RESOURCES)
                .param(PAGE_NO_KEY, DEFAULT_PAGE_NO_STR)
                .param(PAGE_SIZE_KEY, DEFAULT_PAGE_SIZE_STR)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(resourceNum)));
  }
}
