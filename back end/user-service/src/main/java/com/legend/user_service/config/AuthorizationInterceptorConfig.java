package com.legend.user_service.config;

import com.legend.common_util.config.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AuthorizationInterceptor.class)
public class AuthorizationInterceptorConfig {}
