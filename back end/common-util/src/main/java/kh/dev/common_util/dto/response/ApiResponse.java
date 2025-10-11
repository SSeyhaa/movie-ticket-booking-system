package kh.dev.common_util.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"code", "status", "message", "timestamp", "data"})
public class ApiResponse<T> {

  private final int code;
  private final String status;
  private final String message;
  private final ZonedDateTime timestamp;
  private final T data;

  public ApiResponse(int code, String status, String message, T data) {
    this.code = code;
    this.status = status;
    this.message = message;
    this.data = data;
    this.timestamp = ZonedDateTime.now();
  }

  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>(200, "OK", message, data);
  }

  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(200, "OK", "Request successful", data);
  }

  public static <T> ApiResponse<T> error(int code, String message) {
    return new ApiResponse<>(code, "ERROR", message, null);
  }
}
