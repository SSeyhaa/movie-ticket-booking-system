package kh.dev.common_util.util;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class DateTimeUtil {
  public static boolean isDateExcludeTime(LocalDateTime date) {
    return date.toLocalTime().equals(LocalDateTime.MIN.toLocalTime());
  }

  public static boolean isDateIncludeTime(LocalDateTime date) {
    return !isDateExcludeTime(date);
  }
}
