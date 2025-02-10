package com.legend.common_util.constant.notification;

import com.legend.common_util.util.ExecutionContext;

public interface Template {

  String getSubject();

  String getFileName();

  Object getParams(String recipient, ExecutionContext metadata);
}
