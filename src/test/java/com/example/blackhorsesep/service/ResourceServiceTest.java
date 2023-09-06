package com.example.blackhorsesep.service;

import static com.example.blackhorsesep.TestData.initResourceModelList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.example.blackhorsesep.BaseUnitTest;
import com.example.blackhorsesep.integration.CoursePlatformClient;
import com.example.blackhorsesep.model.ResourceModel;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ResourceServiceTest extends BaseUnitTest {

  private static final int DEFAULT_PAGE_NO = 1;

  private static final int DEFAULT_PAGE_SIZE = 10;

  private static final String DEFAULT_RESOURCE_ID = "1";

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
}
