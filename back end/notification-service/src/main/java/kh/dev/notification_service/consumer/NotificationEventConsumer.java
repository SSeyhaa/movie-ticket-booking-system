package kh.dev.notification_service.consumer;

import java.util.function.Consumer;
import kh.dev.common_util.dto.request.NotificationRequest;
import kh.dev.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventConsumer {
  private final NotificationService notificationService;

  @Bean
  public Consumer<NotificationRequest> consumeNotificationEvent() {
    return notificationEvent -> {
      log.info("----- Received notification event: {}", notificationEvent);
      notificationService.send(notificationEvent);
    };
  }
}
