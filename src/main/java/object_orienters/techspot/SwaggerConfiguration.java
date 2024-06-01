package object_orienters.techspot;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    // @Bean
    // public OpenAPI openAPI() {
    //     return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
    //             .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
    //             .info(new Info().title("My REST API")
    //                     .description("Some custom description of API.")
    //                     .version("1.0").contact(new Contact().name("Sallo Szrajbman")
    //                             .email("www.baeldung.com").url("salloszraj@gmail.com"))
    //                     .license(new License().name("License of API")
    //                             .url("API license URL")));
    // }

    // private SecurityScheme createAPIKeyScheme() {
    //     return new SecurityScheme().type(SecurityScheme.Type.HTTP)
    //             .bearerFormat("JWT")
    //             .scheme("bearer");
    // }

}
