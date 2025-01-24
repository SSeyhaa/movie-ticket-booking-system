package com.legend.movie_service.config;

import com.legend.common_util.config.AuthorizationInterceptor;
import com.legend.common_util.config.CorsConfig;
import com.legend.common_util.config.CustomAuthenticationEntryPoint;
import com.legend.common_util.config.JwtConverter;
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
