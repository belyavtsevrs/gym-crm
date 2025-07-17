package com.epam.gymcore.service;

import com.epam.gymcore.domain.model.Training;
import com.epam.gymcore.service.api.Creator;
import com.epam.gymcore.service.api.Retriever;

import java.util.List;

public interface TrainingService extends
        Creator<Training>, Retriever<Training,Long> {
    List<Training> getTrainingByTrainee(Long traineeId);
    List<Training> getTrainingByTrainer(Long trainerId);
}
