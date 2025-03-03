package kh.dev.user_service.config;

import kh.dev.common_util.annotation.aop.StringProcessorAspect;
import kh.dev.common_util.util.SchedulerUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({StringProcessorAspect.class, SchedulerUtils.class})
public class AppConfig {}
