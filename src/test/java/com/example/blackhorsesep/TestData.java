package com.example.blackhorsesep;

import com.example.blackhorsesep.model.ResourceModel;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TestData {

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
