package cl.bakery.Usuarios.Config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration

@OpenAPIDefinition(
    info = @io.swagger.v3.oas.annotations.info.Info(title = "Pasteleria Mil sabores - B2C Tech - DUOC 2025", version = "2.0",
        description = "Documentación de la API de Usuarios"),
    security = @SecurityRequirement(name = "apiKey") // Esto aplica la API Key globalmente
)
@SecurityScheme(
    name = "apiKey",
    type = SecuritySchemeType.APIKEY,
    in = SecuritySchemeIn.HEADER,
    paramName = "X-API-KEY"
)

public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }
}
