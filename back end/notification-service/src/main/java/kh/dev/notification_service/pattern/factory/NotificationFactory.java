package kh.dev.notification_service.pattern.factory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import kh.dev.common_util.constant.NotificationType;
import kh.dev.notification_service.pattern.strategy.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

  private final Map<NotificationType, Notification> notificationMap;

  public NotificationFactory(List<Notification> notificationMap) {
    this.notificationMap =
        notificationMap.stream()
            .collect(Collectors.toMap(Notification::getType, Function.identity()));
  }

  public Notification getNotification(NotificationType type) {
    return Optional.ofNullable(notificationMap.get(type))
        .orElseThrow(() -> new IllegalArgumentException("Unsupported notification type"));
  }
}
