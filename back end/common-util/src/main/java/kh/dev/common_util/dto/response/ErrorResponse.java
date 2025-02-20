package kh.dev.common_util.dto.response;

import java.time.LocalDateTime;
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
public class ErrorResponse {
  private int status;
  private String error;
  private String message;
  private String path;
  private String exceptionType;
  private LocalDateTime timestamp;
}
