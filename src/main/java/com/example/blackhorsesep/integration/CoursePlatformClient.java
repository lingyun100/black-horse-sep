package com.example.blackhorsesep.integration;

import com.example.blackhorsesep.exception.CoursePlatformException;
import com.example.blackhorsesep.model.ResourceModel;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author linyun.xie
 */
@Component
@Slf4j
public class CoursePlatformClient {
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${course.platform.base.path}")
  private String coursePlatformBasePath;

  public List<ResourceModel> getResources(String rid, int pageNo, int pageSize) {
    Map<String, String> params = new HashMap<>(2);
    params.put("pageNo", String.valueOf(pageNo));
    params.put("pageSize", String.valueOf(pageSize));

    String url =
        coursePlatformBasePath + rid + "/resources" + "?pageNo={pageNo}&pageSize={pageSize}";
    ResponseEntity<ResourceModel[]> response;
    try {
      response = restTemplate.getForEntity(url, ResourceModel[].class, params);
    } catch (Exception e) {
      log.error("call course platform error", e);
      throw new CoursePlatformException(e.getMessage());
    }

    return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
  }
}
