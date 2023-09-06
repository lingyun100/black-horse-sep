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
import org.junit.jupiter.api.Test;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

class CoursePlatformClientTest extends BaseIntegrationTest {

  @Autowired CoursePlatformClient coursePlatformClient;

  @Autowired ObjectMapper objectMapper;

  @Test
  @SneakyThrows
  void should_return_8_resources_when_get_resources_given_8_resources_from_fake_course_platform() {
    List<ResourceModel> resourceModelList = initResourceModelList(8);
    mockServerClient
        .when(
            request().withMethod(GET.name()).withPath("/course-platform/1/resources"), unlimited())
        .respond(
            response()
                .withStatusCode(200)
                .withContentType(MediaType.APPLICATION_JSON)
                .withBody(objectMapper.writeValueAsString(resourceModelList))
                .withDelay(TimeUnit.MILLISECONDS, 1000));

    List<ResourceModel> resources = coursePlatformClient.getResources("1", 1, 10);
    assertThat(resources).hasSize(8);
  }
}
