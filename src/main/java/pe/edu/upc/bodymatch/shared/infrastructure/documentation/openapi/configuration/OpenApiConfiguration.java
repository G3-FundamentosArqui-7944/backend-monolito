package pe.edu.upc.bodymatch.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI bodyMatchOpenApi() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(apiInfo())
                .externalDocs(externalDocs())
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    private Info apiInfo() {
        return new Info()
                .title("BodyMatch AI API")
                .description("REST API documentation for BodyMatch AI, an intelligent fitness platform that connects athletes with coaches through artificial intelligence.")
                .version("v1.0.0")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .contact(new Contact()
                        .name("BodyMatch AI Team")
                        .url("https://github.com/bodymatch-ai")
                        .email("contact@bodymatch.ai"));
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description("BodyMatch AI External Documentation")
                .url("https://github.com/bodymatch-ai/docs");
    }
}