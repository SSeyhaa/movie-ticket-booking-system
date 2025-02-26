package kh.dev.common_util.constant.notification;

import kh.dev.common_util.constant.FileExtension;
import kh.dev.common_util.constant.NotificationTemplate;
import kh.dev.common_util.constant.UserConstants;
import kh.dev.common_util.dto.UserPropertyJteParam;
import kh.dev.common_util.util.ExecutionContext;

public class UserPasswordUpdatedTemplate implements Template {

  public static final Template INSTANCE = new UserPasswordUpdatedTemplate();

  @Override
  public String getSubject() {
    return "Security Alert: Your Password Has Been Changed";
  }

  @Override
  public String getFileName() {
    return NotificationTemplate.USER_PASSWORD_UPDATE.toString().concat(FileExtension.JTE);
  }

  @Override
  public Object getParams(String recipient, ExecutionContext metadata) {
    String firstName = metadata.get(UserConstants.FIRST_NAME, String.class);
    String lastName = metadata.get(UserConstants.LAST_NAME, String.class);

    UserPropertyJteParam userRegistrationJteParam = new UserPropertyJteParam();
    userRegistrationJteParam.setFirstName(firstName);
    userRegistrationJteParam.setLastName(lastName);
    userRegistrationJteParam.setEmail(recipient);
    return userRegistrationJteParam;
  }
}
