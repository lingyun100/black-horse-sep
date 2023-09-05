package com.example.blackhorsesep.model;

import java.time.LocalDateTime;
import lombok.*;

/**
 * @author linyun.xie
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceModel {
  private String resourceName;
  private LocalDateTime createDate;
  private LocalDateTime updatedDate;
}
