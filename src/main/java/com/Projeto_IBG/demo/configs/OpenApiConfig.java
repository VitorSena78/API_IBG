package com.Projeto_IBG.demo.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Sistema de Saúde")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de pacientes e especialidades médicas")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("dev@saude.com")
                                .url("https://saude.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
