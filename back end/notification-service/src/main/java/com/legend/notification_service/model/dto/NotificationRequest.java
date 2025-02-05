package com.legend.notification_service.model.dto;

import com.legend.common_util.constant.NotificationTemplate;
import com.legend.common_util.constant.NotificationType;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NotificationRequest {

  private String recipient;
  private NotificationType type;
  private NotificationTemplate template;
  private Map<String, Object> metadata;
}
