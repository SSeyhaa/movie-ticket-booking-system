package com.legend.user_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(com.legend.common_util.config.CorsConfig.class)
public class UserServiceCorsConfig {}
