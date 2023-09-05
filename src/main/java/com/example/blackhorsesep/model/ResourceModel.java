package com.example.blackhorsesep.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author linyun.xie
 */
@Getter
@Setter
@Builder
public class ResourceModel {
  private String resourceName;
  private LocalDateTime createDate;
  private LocalDateTime updatedDate;
}
