package com.example.blackhorsesep.service;

import static com.example.blackhorsesep.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.example.blackhorsesep.BaseUnitTest;
import com.example.blackhorsesep.exception.CoursePlatformException;
import com.example.blackhorsesep.integration.CoursePlatformClient;
import com.example.blackhorsesep.model.ResourceModel;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ResourceServiceTest extends BaseUnitTest {

  @Mock private CoursePlatformClient coursePlatformClient;

  @InjectMocks private ResourceService resourceService;

  @Test
  void should_return_8_resources_when_get_resource_given_8_resources_from_integration() {
    int resourceNum = 8;
    when(coursePlatformClient.getResources(anyString(), anyInt(), anyInt()))
        .thenReturn(initResourceModelList(resourceNum));

    List<ResourceModel> resources =
        resourceService.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);

    assertThat(resources).hasSize(resourceNum);
  }

  @Test
  void should_return_10_resources_when_get_resource_given_10_resources_from_integration() {
    int resourceNum = 10;
    when(coursePlatformClient.getResources(anyString(), anyInt(), anyInt()))
        .thenReturn(initResourceModelList(resourceNum));

    List<ResourceModel> resources =
        resourceService.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);

    assertThat(resources).hasSize(resourceNum);
  }

  @Test
  void should_return_0_resources_when_get_resource_given_0_resources_from_integration() {
    int resourceNum = 0;
    when(coursePlatformClient.getResources(anyString(), anyInt(), anyInt()))
        .thenReturn(initResourceModelList(resourceNum));

    List<ResourceModel> resources =
        resourceService.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);

    assertThat(resources).hasSize(resourceNum);
  }

  @Test
  void should_throw_CoursePlatformException_when_get_resource_given_exception_from_integration() {
    when(coursePlatformClient.getResources(anyString(), anyInt(), anyInt()))
        .thenThrow(new CoursePlatformException("server error"));

    assertThrows(
        CoursePlatformException.class,
        () ->
            resourceService.getResources(DEFAULT_RESOURCE_ID, DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE));
  }
}
