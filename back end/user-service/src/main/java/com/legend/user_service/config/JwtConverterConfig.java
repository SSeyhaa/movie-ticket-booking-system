package com.legend.user_service.config;

import com.legend.common_util.config.JwtConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JwtConverter.class)
public class JwtConverterConfig {}
