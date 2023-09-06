package com.example.blackhorsesep;

import com.example.blackhorsesep.model.ResourceModel;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TestData {

  public static final int DEFAULT_PAGE_NO = 1;

  public static final int DEFAULT_PAGE_SIZE = 10;

  public static final int DEFAULT_DELAY_MILLISECONDS = 1000;

  public static final String DEFAULT_RESOURCE_ID = "1";

  public static final String DEFAULT_PAGE_NO_STR = String.valueOf(DEFAULT_PAGE_NO);

  public static final String DEFAULT_PAGE_SIZE_STR = String.valueOf(DEFAULT_PAGE_SIZE);

  public static final String PAGE_NO_KEY = "pageNo";

  public static final String PAGE_SIZE_KEY = "pageSize";

  public static final String COURSE_PLATFORM_URL =
      "/course-platform/" + DEFAULT_RESOURCE_ID + "/resources";

  public static List<ResourceModel> initResourceModelList(int num) {
    if (num <= 0) {
      return Collections.emptyList();
    }

    return Stream.iterate(0, i -> i + 1)
        .limit(num)
        .map(i -> initResourceModelWithName("Resource " + i))
        .toList();
  }

  private static ResourceModel initResourceModelWithName(String name) {
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
