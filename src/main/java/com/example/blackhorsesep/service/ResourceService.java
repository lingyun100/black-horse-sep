package com.example.blackhorsesep.service;

import com.example.blackhorsesep.integration.CoursePlatformClient;
import com.example.blackhorsesep.model.ResourceModel;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author linyun.xie
 */
@Service
@RequiredArgsConstructor
public class ResourceService {
  private final CoursePlatformClient coursePlatformClient;

  public List<ResourceModel> getResources(String rid, int pageNo, int pageSize) {
    return coursePlatformClient.getResources(rid, pageNo, pageSize);
  }
}
