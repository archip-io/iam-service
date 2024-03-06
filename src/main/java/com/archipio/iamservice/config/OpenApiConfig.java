package com.archipio.iamservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info =
        @Info(
            title = "IAM Service",
            description =
                "IAM Service (Identity and Access Management Service) - это микросервис, который отвечает за "
                    + "управление идентификацией и доступом пользователей",
            version = "0.0.0"),
    servers = {
      @Server(description = "Server URL in Development environment", url = "http://localhost:18081")
    })
@Configuration
public class OpenApiConfig {}
