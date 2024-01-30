package com.sse.demo.config;

import com.sse.demo.sse.ServerSentEventWebFluxAPI;
import io.swagger.v3.core.util.PrimitiveType;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
@OpenAPIDefinition
@Configuration
public class OpenApiConfiguration {
  @Bean
  public OpenAPI customOpenApi() {
    PrimitiveType.customClasses().put(LocalTime.class.getName(), PrimitiveType.PARTIAL_TIME);
    return new OpenAPI().components(new Components())
      .info(new Info().title("sse-demo").description("Server Sent Event demo").version("1.0.0")
        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")));
  }

  @Bean
  public GroupedOpenApi groupOpenApiLocal() {
    return GroupedOpenApi.builder().group("v1").packagesToScan(ServerSentEventWebFluxAPI.class.getPackageName()).build();
  }
}
