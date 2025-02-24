package kh.dev.user_service.config;

import kh.dev.user_service.client.KeycloakClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = {KeycloakClient.class})
public class ClientConfig {}
