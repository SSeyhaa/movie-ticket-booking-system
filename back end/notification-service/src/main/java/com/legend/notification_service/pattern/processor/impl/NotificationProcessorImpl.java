package com.legend.notification_service.pattern.processor.impl;

import com.legend.common_util.util.ExecutionContext;
import com.legend.notification_service.constant.NotificationConstant;
import com.legend.notification_service.model.dto.NotificationRequest;
import com.legend.notification_service.pattern.strategy.Notification;
import com.legend.notification_service.pattern.factory.NotificationFactory;
import com.legend.notification_service.pattern.processor.NotificationProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StandardNotificationProcessor extends NotificationProcessor {

  private final NotificationFactory notificationFactory;

  @Override
  protected boolean validate(ExecutionContext context) {
    NotificationRequest notification =
        context.get(NotificationConstant.NOTIFICATION_REQUEST, NotificationRequest.class);

    log.info(
        "--- [{}]: Validating notification: {}",
        notification.getTemplate(),
        notification.getRecipient());

    Notification notificationInstance = notificationFactory.getNotification(notification.getType());
    boolean isValid = notificationInstance.isValid(context);

    log.info(
        "--- [{}]: Validation result for notification {}: {}",
        notification.getTemplate(),
        notification.getRecipient(),
        isValid);
    return isValid;
  }

  @Override
  protected void preProcess(ExecutionContext context) {
    NotificationRequest notification =
        context.get(NotificationConstant.NOTIFICATION_REQUEST, NotificationRequest.class);

    log.info(
        "--- [{}]: Pre-processing notification: {}",
        notification.getTemplate(),
        notification.getRecipient());
  }

  @Override
  protected boolean send(ExecutionContext context) {
    NotificationRequest notification =
        context.get(NotificationConstant.NOTIFICATION_REQUEST, NotificationRequest.class);

    log.info(
        "--- [{}]: Sending notification: {}",
        notification.getTemplate(),
        notification.getRecipient());
    Notification notificationInstance = notificationFactory.getNotification(notification.getType());
    boolean sendResult = notificationInstance.send(context);

    log.info(
        "--- [{}]: Send result for notification {}: {}",
        notification.getTemplate(),
        notification.getRecipient(),
        sendResult);
    return sendResult;
  }

  @Override
  protected void postProcess(ExecutionContext context, boolean result) {
    NotificationRequest notification =
        context.get(NotificationConstant.NOTIFICATION_REQUEST, NotificationRequest.class);

    log.info(
        "--- [{}]: Post-processing notification: {}, result: {}",
        notification.getTemplate(),
        notification.getRecipient(),
        result);
  }

  @Override
  protected void handleError(ExecutionContext context, Exception error) {
    NotificationRequest notification =
        context.get(NotificationConstant.NOTIFICATION_REQUEST, NotificationRequest.class);

    log.error(
        "--- [{}]: Error handling notification: {}, error: {}",
        notification.getTemplate(),
        notification.getRecipient(),
        error.getMessage(),
        error);
  }
}
