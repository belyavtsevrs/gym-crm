package com.epam.gymcore.integrationalTests;

import com.epam.gymcore.dao.TrainingTypeDao;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.TrainingType;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.TrainerService;
import com.epam.gymcore.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainingServiceIntegrationTest {
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TraineeService traineeService;
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainingTypeDao trainingTypeDao;

    @Test
    void shouldBeCreated(){
        TrainingType trainingType = trainingTypeDao.findByName("bodybuilding")
                .orElseGet(() -> {
                    TrainingType newType = new TrainingType("bodybuilding");
                    trainingTypeDao.save(newType);
                    return newType;
                });


        Trainee trainee = traineeService.findByUsername("rodion.b").get();
        Trainer trainer = trainerService.findByUsername("vasily.che").get();

        assertNotNull(trainingType);

        Training training = new Training(trainer,
                trainee,
                trainingType,
                LocalDateTime.now(),
                Duration.ofHours(2)
        );

        Training created = trainingService.create(training);

        assertNotNull(created);
        assertNotNull(created.getId());
    }

    @Test
    void shouldFindByTrainee(){
        List<Training> trainingList = trainingService.getTrainingByTrainee(1l);

        assertNotEquals(0,trainingList.size());
    }
}
