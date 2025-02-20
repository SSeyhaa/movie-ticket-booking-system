package kh.dev.booking_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(kh.dev.common_util.config.SwaggerUIRedirection.class)
public class SwaggerUIRedirection {}
