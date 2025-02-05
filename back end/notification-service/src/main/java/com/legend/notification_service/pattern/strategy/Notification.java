package com.legend.notification_service.pattern.strategy;

import com.legend.common_util.constant.NotificationType;
import com.legend.common_util.util.ExecutionContext;

public interface Notification {

  NotificationType getType();

  boolean isValid(ExecutionContext context);

  boolean send(ExecutionContext context);
}
