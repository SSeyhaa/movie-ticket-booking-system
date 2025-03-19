package kh.dev.common_util.dto.response;

import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Response {
  private int code;
  private String status;
  private String message;
  private String body;
  private ZonedDateTime timestamp;
}
