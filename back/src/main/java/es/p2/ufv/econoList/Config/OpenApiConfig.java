package es.p2.ufv.econoList.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "EconoList API",
                version = "1.0",
                description = "Documentación de la API de EconoList"
        )
)

public class OpenApiConfig {
        // No hace falta más código; con la anotación es suficiente.
        // http://localhost:8089/swagger-ui/index.html/ (Ruta para ver la documentación)
}
