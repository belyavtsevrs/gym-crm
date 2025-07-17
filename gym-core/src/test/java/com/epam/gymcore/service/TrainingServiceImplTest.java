package com.epam.gymcore.service;

import com.epam.gymcore.dao.TrainingDaoImpl;
import com.epam.gymcore.domain.entity.TrainingImpl;
import com.epam.gymcore.domain.model.Training;
import com.epam.gymcore.service.Impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceImplTest {
    @Mock
    private TrainingDaoImpl trainingDao;
    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training newTraining;
    private long traineeId;
    private long trainerId;

    @BeforeEach
    public void init(){
        traineeId = 1L;
        trainerId = 2L;
        newTraining = new TrainingImpl(
                10L,
                trainerId,
                traineeId,
                3L,
                LocalDateTime.of(2025, 7, 21, 8, 0),
                Duration.ofMinutes(90)
        );
    }

    @Test
    void testCreateTraining() {
        Training result = trainingService.create(newTraining);

        verify(trainingDao, times(1)).save(newTraining);

        assertEquals(newTraining, result);
    }

    @Test
    void shouldReturnAll() {
        List<Training> list = List.of(newTraining);
        when(trainingDao.findAll()).thenReturn(list);

        List<Training> result = trainingService.findAll();

        assertFalse(result.isEmpty());
        assertIterableEquals(list, result);
        verify(trainingDao).findAll();
    }

    @Test
    void traineeQuery() {
        List<Training> list = List.of(newTraining);
        when(trainingDao.getTrainingByTrainee(traineeId)).thenReturn(list);

        List<Training> result = trainingService.getTrainingByTrainee(traineeId);

        assertEquals(list, result);
        verify(trainingDao).getTrainingByTrainee(traineeId);
    }

    @Test
    void trainerQuery() {
        List<Training> list = List.of(newTraining);
        when(trainingDao.getTrainingByTrainer(trainerId)).thenReturn(list);

        List<Training> result = trainingService.getTrainingByTrainer(trainerId);

        assertEquals(list, result);
        verify(trainingDao).getTrainingByTrainer(trainerId);
    }
}
