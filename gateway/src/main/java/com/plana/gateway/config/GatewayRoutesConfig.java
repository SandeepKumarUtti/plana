package com.plana.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()

            // Document Manager
            .route("document-manager", r -> r.path("/documents/**")
                    .uri("http://localhost:8082"))

            // Location Based Reminder
            .route("location-reminder", r -> r.path("/reminders/**")
                    .uri("http://localhost:8083"))

            // Todo Service
            .route("todo-service", r -> r.path("/todos/**")
                    .uri("http://localhost:8081"))

            .build();
    }
}
