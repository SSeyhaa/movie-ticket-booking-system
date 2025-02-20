package kh.dev.common_util.constant.notification;

import kh.dev.common_util.util.ExecutionContext;

public interface Template {

  String getSubject();

  String getFileName();

  Object getParams(String recipient, ExecutionContext metadata);
}
