package com.legend.payment_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(com.legend.common_util.config.JpaConfig.class)
public class JpaConfig {}
