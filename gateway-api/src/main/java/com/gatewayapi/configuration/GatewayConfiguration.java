package com.gatewayapi.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

  @Bean
  public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
        .route(r -> r.path("/registration")
          .uri("http://localhost:8081/"))
////        .id("account-service"))
        .route(r -> r.path("/delete/**")
            .filters(rw -> rw.rewritePath("/delete/(?<segment>.*)", "/delete/${segment}"))
          .uri("http://localhost:8081/"))
        .route(r -> r.path("/login")
          .uri("http://localhost:8081/"))
        .route(r -> r.path("/update")
          .uri("http://localhost:8081/"))
        .route(r -> r.path("/get/**")
            .uri("http://localhost:8081/"))
        .route(r -> r.path("/subscribe")
            .uri("http://localhost:8081/"))
        .route(r -> r.path("/subscription/**")
            .uri("http://localhost:8082"))
        .build();

  }
}
