package com.example.blackhorsesep.integration;

import com.example.blackhorsesep.model.ResourceModel;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author linyun.xie
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CoursePlatformClient {
  private final RestTemplateBuilder restTemplateBuilder;

  private RestTemplate restTemplate;

  @Value("${course.platform.base.path}")
  private String coursePlatformBasePath;

  @PostConstruct
  private void initRestTemplate() {
    restTemplate =
        restTemplateBuilder
            .setReadTimeout(Duration.ofSeconds(2))
            .setConnectTimeout(Duration.ofSeconds(10))
            .build();
  }

  @Retryable(retryFor = Exception.class, backoff = @Backoff(multiplier = 2))
  public List<ResourceModel> getResources(String rid, int pageNo, int pageSize) {
    Map<String, String> params = new HashMap<>(2);
    params.put("pageNo", String.valueOf(pageNo));
    params.put("pageSize", String.valueOf(pageSize));

    String url =
        coursePlatformBasePath + rid + "/resources" + "?pageNo={pageNo}&pageSize={pageSize}";
    log.info("start to query from course platform");
    ResponseEntity<ResourceModel[]> response =
        restTemplate.getForEntity(url, ResourceModel[].class, params);
    log.info("query from course platform successfully");
    return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
  }
}
