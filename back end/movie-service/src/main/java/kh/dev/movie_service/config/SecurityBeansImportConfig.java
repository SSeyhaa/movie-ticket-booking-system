package kh.dev.movie_service.config;

import kh.dev.common_util.config.AuthorizationInterceptor;
import kh.dev.common_util.config.CorsConfig;
import kh.dev.common_util.config.CustomAuthenticationEntryPoint;
import kh.dev.common_util.config.JwtConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
  CorsConfig.class,
  JwtConverter.class,
  AuthorizationInterceptor.class,
  CustomAuthenticationEntryPoint.class
})
public class SecurityBeansImportConfig {}
