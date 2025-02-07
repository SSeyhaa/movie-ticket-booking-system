package com.legend.notification_service.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JteConfig {

  @Bean
  public TemplateEngine templateEngine() {
    return TemplateEngine.createPrecompiled(ContentType.Html);
  }
}
