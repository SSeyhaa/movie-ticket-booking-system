package com.legend.notification_service.consumer;

import com.legend.common_util.dto.request.NotificationRequest;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventConsumer {

  @Bean
  public Consumer<NotificationRequest> consumeNotificationEvent() {
    return notificationEvent ->
        log.info("----- Received notification event: {}", notificationEvent);
  }
}
