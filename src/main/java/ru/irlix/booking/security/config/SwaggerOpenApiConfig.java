package ru.irlix.booking.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(name = "Mamonov Kirill",
                        email = "mamonov_2016@bk.ru"),
                description = "OpenApi documentation for REST-api",
                title = "OpenApi specification",
                version = "v1.0"
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local ENV")
        },
        security = @SecurityRequirement(name = "bearerAuth")

)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT")
public class SwaggerOpenApiConfig {
}
