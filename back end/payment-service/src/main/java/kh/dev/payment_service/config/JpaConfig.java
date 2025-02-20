package kh.dev.payment_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(kh.dev.common_util.config.JpaConfig.class)
public class JpaConfig {}
