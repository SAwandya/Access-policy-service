package com.policy.authority.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI policyAuthorityOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Policy Authority Service API")
                .description("REST API for policy management with versioning and event publishing")
                .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }
}
