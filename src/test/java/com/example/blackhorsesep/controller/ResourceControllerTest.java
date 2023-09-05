package com.example.blackhorsesep.controller;

import static com.example.blackhorsesep.constant.ApiConstant.RESOURCES;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCE_BASE;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
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
    String resourceId = "1";
    int resourceNum = 8;
    List<ResourceModel> resourceModelList = initResourceModelWithGivenNumber(resourceNum);
    when(resourceService.getResources(eq(resourceId), anyInt(), anyInt()))
        .thenReturn(resourceModelList);

    mockMvc
        .perform(
            get(RESOURCE_BASE + "/" + resourceId + RESOURCES)
                .param("pageNo", "1")
                .param("pageSize", "10")
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
            get(RESOURCE_BASE + "/1" + RESOURCES)
                .param("pageNo", pageNo)
                .param("pageSize", pageSize)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode", equalTo(PARAM_VALIDATE_FAILURE)));
  }

  private List<ResourceModel> initResourceModelWithGivenNumber(int num) {
    if (num <= 0) {
      return Collections.emptyList();
    }

    return Stream.iterate(0, i -> i + 1)
        .limit(num)
        .map(i -> initResourceModel("Resource " + i))
        .toList();
  }

  private ResourceModel initResourceModel(String name) {
    LocalDateTime now = LocalDateTime.now();
    Random randomGenerator = new Random();
    int maxSeconds = 1000;

    return ResourceModel.builder()
        .resourceName(name)
        .createDate(now.plusSeconds(randomGenerator.nextInt(maxSeconds)))
        .updatedDate(now.plusSeconds(randomGenerator.nextInt(maxSeconds)))
        .build();
  }
}
