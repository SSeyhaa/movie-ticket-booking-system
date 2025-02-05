package com.legend.notification_service.config;

import com.legend.common_util.config.SwaggerUIRedirection;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@RequiredArgsConstructor
@Import(SwaggerUIRedirection.class)
public class SwaggerConfig {

  private final KeycloakProperty keycloakProperty;

  @Bean
  public OpenAPI openAPI() {
    String schemeName = "security flow";
    return new OpenAPI()
        .info(
            new Info()
                .title("NOTIFICATION-SERVICE API")
                .description(
                    """
                        Comprehensive API documentation for handling notifications within the system.
                        This service is responsible for sending email and in-app notifications related to user actions, bookings, payments, and other important events.
                    """)
                .version("1.0.0")
                .termsOfService("https://example.com/terms")
                .contact(
                    new Contact()
                        .name("API Support Team")
                        .email("support@example.com")
                        .url("https://example.com/contact"))
                .license(new License().name("dev.kh License").url("https://example.com/license")))
        .addSecurityItem(new SecurityRequirement().addList(schemeName))
        .components(
            new Components()
                .addSecuritySchemes(
                    schemeName,
                    new SecurityScheme()
                        .name(schemeName)
                        .type(SecurityScheme.Type.OAUTH2)
                        .description("OAuth2 Bearer Token for securing API endpoints")
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
                        .flows(
                            new OAuthFlows()
                                .password(
                                    new OAuthFlow().tokenUrl(keycloakProperty.getTokenUrl())))));
  }
}
