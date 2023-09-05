package com.example.blackhorsesep.controller;

import static com.example.blackhorsesep.constant.ApiConstant.ALL_RESOURCES_API;

import com.example.blackhorsesep.controller.response.Resource;
import com.example.blackhorsesep.model.ResourceModel;
import com.example.blackhorsesep.service.ResourceService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author linyun.xie
 */
@RestController
@RequestMapping(ALL_RESOURCES_API)
@Validated
@RequiredArgsConstructor
@Slf4j
public class ResourceController {
  private final ResourceService resourceService;

  @GetMapping
  public List<Resource> getResources(
      @PathVariable(value = "rid") String rid,
      @RequestParam("pageNo") @Min(1) int pageNo,
      @RequestParam("pageSize") @Min(1) @Max(1000) int pageSize) {
    log.info("start query resources, rid {}, pageNo {}, pageSize {}", rid, pageNo, pageSize);
    List<ResourceModel> resourceModels = resourceService.getResources(rid, pageNo, pageSize);
    if (CollectionUtils.isEmpty(resourceModels)) {
      log.info("resources is empty");
      return Collections.emptyList();
    }

    log.info("resources is not empty");
    return resourceModels.stream()
        .map(
            resourceModel ->
                new Resource(resourceModel.getResourceName(), resourceModel.getCreateDate()))
        .toList();
  }
}
