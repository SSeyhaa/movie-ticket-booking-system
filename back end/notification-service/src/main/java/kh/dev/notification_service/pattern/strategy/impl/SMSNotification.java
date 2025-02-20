package kh.dev.notification_service.pattern.strategy.impl;

import kh.dev.common_util.constant.NotificationType;
import kh.dev.common_util.util.ExecutionContext;
import kh.dev.notification_service.pattern.strategy.Notification;
import org.springframework.stereotype.Component;

@Component
public class SMSNotification implements Notification {

  @Override
  public NotificationType getType() {
    return NotificationType.SMS;
  }

  @Override
  public boolean isValid(ExecutionContext context) {
    return false;
  }

  @Override
  public boolean send(ExecutionContext context) {
    return false;
  }
}
