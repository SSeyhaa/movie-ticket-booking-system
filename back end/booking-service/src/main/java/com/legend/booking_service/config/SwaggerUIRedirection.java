package com.legend.booking_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(com.legend.common_util.config.SwaggerUIRedirection.class)
public class SwaggerUIRedirection {}
