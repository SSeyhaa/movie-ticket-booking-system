package kh.dev.user_service.config;

import kh.dev.common_util.annotation.aop.StringProcessorAspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({StringProcessorAspect.class})
public class AOPConfig {}
