package com.example.blackhorsesep.functionalTest;

import static com.example.blackhorsesep.TestData.*;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCES;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCE_BASE;
import static com.example.blackhorsesep.constant.Constant.URL_PATH;
import static com.example.blackhorsesep.exception.ErrorCodeConstant.COURSE_PLATFORM_ERROR;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.blackhorsesep.BaseIntegrationTest;
import com.example.blackhorsesep.integration.CoursePlatformClient;
import com.example.blackhorsesep.model.ResourceModel;
import com.example.blackhorsesep.service.ResourceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class GetResourcesFunctionalTest extends BaseIntegrationTest {
  @Autowired ObjectMapper objectMapper;
  @Autowired MockMvc mockMvc;
  @Autowired CoursePlatformClient coursePlatformClient;
  @Autowired ResourceService resourceService;

  @AfterEach
  void tearDown() {
    mockServerClient.reset();
  }

  @ParameterizedTest
  @ValueSource(ints = {8, 10, 0})
  @SneakyThrows
  void
      should_return_specify_num_resources_when_get_resources_given_specify_num_resources_from_fake_course_platform(
          int resourceNum) {
    prepareResourcesWhenCallCoursePlatformAPI(resourceNum);

    mockMvc
        .perform(
            get(RESOURCE_BASE + URL_PATH + DEFAULT_RESOURCE_ID + RESOURCES)
                .param(PAGE_NO_KEY, DEFAULT_PAGE_NO_STR)
                .param(PAGE_SIZE_KEY, DEFAULT_PAGE_SIZE_STR)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(resourceNum)));
  }

  @Test
  @SneakyThrows
  void
      should_return_500_and_error_code_COURSE_PLATFORM_ERROR_when_get_resources_given_exception_from_fake_course_platform() {
    mockServerClient
        .when(request().withMethod(GET.name()).withPath(COURSE_PLATFORM_URL), unlimited())
        .respond(
            response()
                .withStatusCode(HttpStatus.REQUEST_TIMEOUT.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 9 * DEFAULT_DELAY_MILLISECONDS));

    mockMvc
        .perform(
            get(RESOURCE_BASE + URL_PATH + DEFAULT_RESOURCE_ID + RESOURCES)
                .param(PAGE_NO_KEY, DEFAULT_PAGE_NO_STR)
                .param(PAGE_SIZE_KEY, DEFAULT_PAGE_SIZE_STR)
                .contentType(APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errorCode", equalTo(COURSE_PLATFORM_ERROR)));
  }

  private void prepareResourcesWhenCallCoursePlatformAPI(int resourceNum)
      throws JsonProcessingException {
    List<ResourceModel> resourceModelList = initResourceModelList(resourceNum);
    mockServerClient
        .when(request().withMethod(GET.name()).withPath(COURSE_PLATFORM_URL), unlimited())
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(objectMapper.writeValueAsString(resourceModelList))
                .withDelay(TimeUnit.MILLISECONDS, 1000));
  }
}
