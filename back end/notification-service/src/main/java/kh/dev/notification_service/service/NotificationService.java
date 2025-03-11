package kh.dev.notification_service.service;

import kh.dev.common_util.dto.request.NotificationRequest;
import kh.dev.common_util.util.ExecutionContext;
import kh.dev.notification_service.constant.NotificationConstant;
import kh.dev.notification_service.pattern.processor.NotificationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

  private final NotificationProcessor notificationProcessor;

  public void send(NotificationRequest notification) {
    ExecutionContext context = new ExecutionContext();
    context.put(NotificationConstant.NOTIFICATION_REQUEST, notification);

    notificationProcessor.process(context);
  }
}
