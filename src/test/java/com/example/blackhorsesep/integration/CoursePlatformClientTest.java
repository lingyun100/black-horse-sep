package com.example.blackhorsesep.integration;

import static com.example.blackhorsesep.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.http.HttpMethod.GET;

import com.example.blackhorsesep.BaseIntegrationTest;
import com.example.blackhorsesep.model.ResourceModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

class CoursePlatformClientTest extends BaseIntegrationTest {

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
    prepareResourcesWhenCallCoursePlatformAPI(resourceNum);

    List<ResourceModel> resources =
        coursePlatformClient.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    assertThat(resources).hasSize(resourceNum);
  }

  @Test
  @SneakyThrows
  void
      should_return_10_resources_when_get_resources_given_10_resources_from_fake_course_platform() {
    int resourcesNum = 10;
    prepareResourcesWhenCallCoursePlatformAPI(resourcesNum);

    List<ResourceModel> resources =
        coursePlatformClient.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    assertThat(resources).hasSize(resourcesNum);
  }

  @Test
  @SneakyThrows
  void should_return_0_resources_when_get_resources_given_0_resources_from_fake_course_platform() {
    int resourcesNum = 0;
    prepareResourcesWhenCallCoursePlatformAPI(resourcesNum);

    List<ResourceModel> resources =
        coursePlatformClient.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
    assertThat(resources).hasSize(resourcesNum);
  }

  private void prepareResourcesWhenCallCoursePlatformAPI(int resourcesNum)
      throws JsonProcessingException {
    List<ResourceModel> resourceModelList = initResourceModelList(resourcesNum);
    mockServerClient
        .when(request().withMethod(GET.name()).withPath(COURSE_PLATFORM_URL), unlimited())
        .respond(
            response()
                .withStatusCode(HttpStatus.OK.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(objectMapper.writeValueAsString(resourceModelList))
                .withDelay(TimeUnit.MILLISECONDS, DEFAULT_DELAY_MILLISECONDS));
  }

  @Test
  @SneakyThrows
  void
      should_throw_InternalServerError_when_get_resources_given_InternalServerError_from_fake_course_platform() {
    mockServerClient
        .when(request().withMethod(GET.name()).withPath(COURSE_PLATFORM_URL), unlimited())
        .respond(
            response()
                .withStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, DEFAULT_DELAY_MILLISECONDS));

    assertThrows(
        HttpServerErrorException.InternalServerError.class,
        () ->
            coursePlatformClient.getResources(
                DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE));
  }
}
