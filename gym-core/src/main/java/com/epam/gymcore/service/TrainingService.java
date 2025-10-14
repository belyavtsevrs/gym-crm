package com.epam.gymcore.service;

import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.service.api.Creator;
import com.epam.gymcore.service.api.Retriever;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainingService extends
        Creator<Training>,
        Retriever<Training,Long>
{
    List<Training> getTrainingByTrainee(Long traineeId);
    List<Training> getTrainingByTrainer(Long trainerId);
    TrainingDto createTraining(CreateTrainingDto trainingDto);
    List<TrainingTypeDto> typesList();
    boolean removeTraining(String username,LocalDateTime dateTime);
}
