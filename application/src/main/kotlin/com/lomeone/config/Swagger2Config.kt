package com.lomeone.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Swagger2Config {
    @Bean
    fun swaggerOpenApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Swagger API")
                .description("Show swagger api")
                .version("v1")
    )
}
