package com.example.blackhorsesep.service;

import com.example.blackhorsesep.exception.CoursePlatformException;
import com.example.blackhorsesep.integration.CoursePlatformClient;
import com.example.blackhorsesep.model.ResourceModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author linyun.xie
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResourceService {
  private final CoursePlatformClient coursePlatformClient;

  public List<ResourceModel> getResources(String rid, int pageNo, int pageSize) {
    try {
      return coursePlatformClient.getResources(rid, pageNo, pageSize);
    } catch (Exception e) {
      log.error("error occur", e);
      throw new CoursePlatformException(e.getMessage());
    }
  }
}
