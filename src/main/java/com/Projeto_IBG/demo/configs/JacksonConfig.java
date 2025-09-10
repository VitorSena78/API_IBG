package com.Projeto_IBG.demo.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Registrar módulo para suporte a LocalDate/LocalDateTime
        mapper.registerModule(new JavaTimeModule());
        
        // Configurar para não usar timestamps para datas
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Outras configurações úteis
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        
        return mapper;
    }
}
