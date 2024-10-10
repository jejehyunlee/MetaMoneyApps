package agile.metamoney.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Agile Team",
                        email = "agile.team@gmail.com"
                ),
                description = "Meta Money API Documentation",
                title = "OpenApi Spesification - Meta Money",
                version = "1.0"
        ),servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "http://localhost:8080"
                )
                /*If use Production Server*/
                /*,@Server(
                        description = "PROD ENV",
                        url = "localhost:8080"
                )*/
}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}
