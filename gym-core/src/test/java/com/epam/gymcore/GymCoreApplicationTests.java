package com.epam.gymcore;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.domain.model.Trainer;
import com.epam.gymcore.domain.model.Training;
import com.epam.gymcore.domain.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GymCoreApplicationTests {

    @Autowired
    @Qualifier("traineeStorage")
    private InMemoryStorage<Trainee> traineeStorage;

    @Autowired
    @Qualifier("trainerStorage")
    private InMemoryStorage<Trainer> trainerStorage;

    @Autowired
    @Qualifier("trainingStorage")
    private InMemoryStorage<Training> trainingStorage;

    @Autowired
    @Qualifier("trainingTypeStorage")
    private InMemoryStorage<TrainingType> trainingTypeStorage;

    @Test
    void contextLoads() {
        assertNotNull(traineeStorage);
        assertFalse(traineeStorage.getStorage().isEmpty());

        assertNotNull(trainerStorage);
        assertFalse(trainerStorage.getStorage().isEmpty());

        assertNotNull(trainingStorage);
        assertFalse(trainingStorage.getStorage().isEmpty());

        assertNotNull(trainingTypeStorage);
        assertFalse(trainingTypeStorage.getStorage().isEmpty());
    }

    @Test
    void loadingTraneeStorageData(){
        var map = traineeStorage.getStorage();

        assertNotNull(map);
        assertFalse(map.isEmpty());
        var trainee = map.get(1l);

        assertEquals("Rodion.B",trainee.getUsername());
    }

    @Test
    void loadingTrainerStorageData(){
        var map = trainerStorage.getStorage();

        assertNotNull(map);
        assertFalse(map.isEmpty());
        var trainee = map.get(1l);

        assertEquals("Valisy.Che",trainee.getUsername());
    }

    @Test
    void loadingTrainingStorageData(){
        var training = trainingStorage.getStorage().get(1l);

        assertNotNull(training);

        Trainer trainer = trainerStorage.getStorage().get(training.getTrainerId());
        Trainee trainee = traineeStorage.getStorage().get(training.getTraineeId());
        TrainingType trainingType = trainingTypeStorage.getStorage().get(training.getTrainingTypeId());

        assertEquals("Valisy.Che",trainer.getUsername());
        assertEquals("Rodion.B",trainee.getUsername());
        assertEquals("youga",trainingType.getName());
    }

}
