package kh.dev.common_util.constant;

import kh.dev.common_util.constant.notification.Template;
import kh.dev.common_util.constant.notification.UserPasswordUpdatedTemplate;
import kh.dev.common_util.constant.notification.UserRegistrationTemplate;
import kh.dev.common_util.util.ExecutionContext;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotificationTemplate implements Template {
  USER_REGISTRATION(UserRegistrationTemplate.INSTANCE),
  USER_PASSWORD_UPDATE(UserPasswordUpdatedTemplate.INSTANCE),
  ;

  private final Template template;

  @Override
  public String getSubject() {
    return template.getSubject();
  }

  @Override
  public String getFileName() {
    return template.getFileName();
  }

  @Override
  public Object getParams(String recipient, ExecutionContext metadata) {
    return template.getParams(recipient, metadata);
  }
}
