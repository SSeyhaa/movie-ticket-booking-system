package kh.dev.common_util.constant;

import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublicEndpoint {

  public static final String[] SWAGGER_ENDPOINTS = {"/swagger-ui/**", "/v3/api-docs*/**"};
  public static final String[] ACTUATOR_ENDPOINTS = {"/actuator/**"};

  public static final String[] ALL_PUBLIC_ENDPOINTS =
      concatArrays(new String[] {"/"}, SWAGGER_ENDPOINTS, ACTUATOR_ENDPOINTS);

  private static String[] concatArrays(String[]... arrays) {
    return Stream.of(arrays).flatMap(Stream::of).toArray(String[]::new);
  }
}
