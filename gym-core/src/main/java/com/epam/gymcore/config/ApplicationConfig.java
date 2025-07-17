package com.epam.gymcore.config;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.entity.TraineeImpl;
import com.epam.gymcore.domain.entity.TrainerImpl;
import com.epam.gymcore.domain.entity.TrainingImpl;
import com.epam.gymcore.domain.entity.TrainingTypeImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.domain.model.Trainer;
import com.epam.gymcore.domain.model.Training;
import com.epam.gymcore.domain.model.TrainingType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${data.traineeData}")
    private String traineeData;

    @Value("${data.trainerData}")
    private String trainerData;

    @Value("${data.trainingData}")
    private String trainingData;

    @Value("${data.trainingTypeData}")
    private String trainingTypeData;

    @Bean
    public InMemoryStorage<Trainee> traineeStorage(){
        return new InMemoryStorage(traineeData, TraineeImpl.class);
    }

    @Bean
    public InMemoryStorage<Trainer> trainerStorage() {
        return new InMemoryStorage<>(trainerData, TrainerImpl.class);
    }

    @Bean
    public InMemoryStorage<Training> trainingStorage() {
        return new InMemoryStorage<>(trainingData, TrainingImpl.class);
    }

    @Bean
    public InMemoryStorage<TrainingType> trainingTypeStorage() {
        return new InMemoryStorage<>(trainingTypeData, TrainingTypeImpl.class);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
