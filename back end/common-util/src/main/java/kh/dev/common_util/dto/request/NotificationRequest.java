package kh.dev.common_util.dto.request;

import kh.dev.common_util.constant.NotificationTemplate;
import kh.dev.common_util.constant.NotificationType;
import kh.dev.common_util.util.ExecutionContext;
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
  private ExecutionContext metadata;
}
