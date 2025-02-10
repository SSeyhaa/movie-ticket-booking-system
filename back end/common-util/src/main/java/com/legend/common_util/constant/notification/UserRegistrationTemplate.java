package com.legend.common_util.constant.notification;

import com.legend.common_util.constant.FileExtension;
import com.legend.common_util.constant.NotificationTemplate;
import com.legend.common_util.constant.UserConstants;
import com.legend.common_util.dto.UserRegistrationJteParam;
import com.legend.common_util.util.ExecutionContext;

public class UserRegistrationTemplate implements Template {

  public static final Template INSTANCE = new UserRegistrationTemplate();

  @Override
  public String getSubject() {
    return "User Registration Successful";
  }

  @Override
  public String getFileName() {
    return NotificationTemplate.USER_REGISTRATION.toString().concat(FileExtension.JTE);
  }

  @Override
  public Object getParams(String recipient, ExecutionContext metadata) {
    String firstName = metadata.get(UserConstants.FIRST_NAME, String.class);
    String lastName = metadata.get(UserConstants.LAST_NAME, String.class);

    UserRegistrationJteParam userRegistrationJteParam = new UserRegistrationJteParam();
    userRegistrationJteParam.setFirstName(firstName);
    userRegistrationJteParam.setLastName(lastName);
    userRegistrationJteParam.setEmail(recipient);
    return userRegistrationJteParam;
  }
}
