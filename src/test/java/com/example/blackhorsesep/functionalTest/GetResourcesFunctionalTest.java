package com.example.blackhorsesep.functionalTest;

import static com.example.blackhorsesep.TestData.*;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCES;
import static com.example.blackhorsesep.constant.ApiConstant.RESOURCE_BASE;
import static com.example.blackhorsesep.constant.Constant.URL_PATH;
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
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

  @Test
  @SneakyThrows
  void should_return_8_resources_when_get_resources_given_8_resources_from_fake_course_platform() {
    int resourceNum = 8;
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
      should_return_10_resources_when_get_resources_given_10_resources_from_fake_course_platform() {
    int resourceNum = 10;
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
  void should_return_0_resources_when_get_resources_given_0_resources_from_fake_course_platform() {
    int resourceNum = 0;
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
