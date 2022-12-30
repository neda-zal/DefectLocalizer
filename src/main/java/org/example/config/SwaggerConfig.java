package org.example.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.example.controller"})
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(this.info())
        .externalDocs(this.externalDocs());
  }

  private ExternalDocumentation externalDocs() {
    return new ExternalDocumentation()
        .description("SpringShop Wiki Documentation")
        .url("https://springshop.wiki.github.org/docs");
  }

  private Info info() {
    return new Info()
        .title("Defect Localyzer backend API")
        .description("Defect Localyzer backend API application")
        .version("1.0.0")
        .license(new License().name("Apache 2.0").url("http://springdoc.org"));
  }

}
