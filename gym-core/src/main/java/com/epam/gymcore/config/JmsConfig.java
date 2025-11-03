package com.epam.gymcore.config;

import com.epam.gymcore.domain.dto.RegisterRequest;
import com.epam.gymcore.domain.dto.TrainerRequest;
import com.epam.gymcore.domain.dto.TrainerWorkloadRequest;
import com.epam.gymcore.domain.dto.TrainerWorkloadResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJms
public class JmsConfig {

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");

        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("registerRequest", RegisterRequest.class);
        mappings.put("trainerWorkloadRequest", TrainerWorkloadRequest.class);
        mappings.put("trainerWorkloadResponse", TrainerWorkloadResponse.class);
        mappings.put("trainerRequest", TrainerRequest.class);
        converter.setTypeIdMappings(mappings);

        return converter;
    }
}