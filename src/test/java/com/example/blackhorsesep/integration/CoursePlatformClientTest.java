package com.example.blackhorsesep.integration;

import static com.example.blackhorsesep.TestData.initResourceModelList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.GET;

import com.example.blackhorsesep.BaseIntegrationTest;
import com.example.blackhorsesep.model.ResourceModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class CoursePlatformClientTest extends BaseIntegrationTest {

  private static final int DEFAULT_PAGE_NO = 1;

  private static final int DEFAULT_PAGE_SIZE = 10;

  private static final int DEFAULT_DELAY_MILLISECONDS = 1000;

  private static final String DEFAULT_RESOURCE_ID = "1";

  private static final String COURSE_PLATFORM_URL =
      "/course-platform/" + DEFAULT_RESOURCE_ID + "/resources";

  @Autowired CoursePlatformClient coursePlatformClient;

  @Autowired ObjectMapper objectMapper;

  @AfterEach
  void tearDown() {
    mockServerClient.reset();
  }

  @Test
  @SneakyThrows
  void should_return_8_resources_when_get_resources_given_8_resources_from_fake_course_platform() {
    int resourceNum = 8;
    List<ResourceModel> resourceModelList = initResourceModelList(resourceNum);
    mockServerClient
        .when(request().withMethod(GET.name()).withPath(COURSE_PLATFORM_URL), unlimited())
        .respond(
            response()
                .withStatusCode(HttpStatus.OK.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(objectMapper.writeValueAsString(resourceModelList))
                .withDelay(TimeUnit.MILLISECONDS, DEFAULT_DELAY_MILLISECONDS));

    List<ResourceModel> resources =
        coursePlatformClient.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    assertThat(resources).hasSize(resourceNum);
  }

  @Test
  @SneakyThrows
  void
      should_return_10_resources_when_get_resources_given_10_resources_from_fake_course_platform() {
    int resourcesNum = 10;
    List<ResourceModel> resourceModelList = initResourceModelList(resourcesNum);
    mockServerClient
        .when(request().withMethod(GET.name()).withPath(COURSE_PLATFORM_URL), unlimited())
        .respond(
            response()
                .withStatusCode(HttpStatus.OK.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(objectMapper.writeValueAsString(resourceModelList))
                .withDelay(TimeUnit.MILLISECONDS, DEFAULT_DELAY_MILLISECONDS));

    List<ResourceModel> resources =
        coursePlatformClient.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    assertThat(resources).hasSize(resourcesNum);
  }
}
