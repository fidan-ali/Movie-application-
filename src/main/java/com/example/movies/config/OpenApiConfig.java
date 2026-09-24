package com.example.movies.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Movie API",
                description = "Movie discovery and watchlist application integrating with TMDB",
                version = "v1"
        ),
        security = @SecurityRequirement(name = "userId")
)
@SecurityScheme(
        name = "userId",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "userId",
        description = "Identifies the calling user. Not a real auth mechanism yet."
)
public class OpenApiConfig {
}