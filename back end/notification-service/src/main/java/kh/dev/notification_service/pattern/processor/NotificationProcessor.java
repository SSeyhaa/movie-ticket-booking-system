package kh.dev.notification_service.pattern.processor;

import kh.dev.common_util.util.ExecutionContext;

public abstract class NotificationProcessor {

  protected abstract boolean validate(ExecutionContext context);

  protected abstract void preProcess(ExecutionContext context);

  protected abstract boolean send(ExecutionContext context);

  protected abstract void postProcess(ExecutionContext context, boolean result);

  protected abstract void handleError(ExecutionContext context, Exception error);

  public void process(ExecutionContext context) {
    try {
      if (!validate(context)) {
        throw new IllegalArgumentException("Invalid notification payload");
      }

      preProcess(context);

      boolean result = send(context);

      postProcess(context, result);

    } catch (Exception e) {
      handleError(context, e);
    }
  }
}
