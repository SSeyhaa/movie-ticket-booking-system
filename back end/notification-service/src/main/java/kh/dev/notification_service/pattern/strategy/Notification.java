package kh.dev.notification_service.pattern.strategy;

import kh.dev.common_util.constant.NotificationType;
import kh.dev.common_util.util.ExecutionContext;

public interface Notification {

  NotificationType getType();

  boolean isValid(ExecutionContext context);

  boolean send(ExecutionContext context);
}
