package com.epam.gymcore.service.Impl;

import com.epam.gymcore.dao.TraineeDaoImpl;
import com.epam.gymcore.dao.TrainingDaoImpl;
import com.epam.gymcore.domain.entity.TrainingImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.domain.model.Training;
import com.epam.gymcore.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingServiceImpl extends AbstractService<Training, TrainingDaoImpl> implements TrainingService {

    public TrainingServiceImpl(TrainingDaoImpl dao) {
        super(dao);
    }

    @Override
    public Training create(Training entity) {
        log.info("Creating new training: {}", entity);
        dao.save(entity);
        log.debug("Training created successfully: {}", entity);
        return entity;
    }

    @Override
    public List<Training> getTrainingByTrainee(Long traineeId) {
        log.info("Fetching trainings for trainee with id: {}", traineeId);
        List<Training> trainings = dao.getTrainingByTrainee(traineeId);
        log.debug("trainings", trainings.size(), traineeId);
        return trainings;
    }

    @Override
    public List<Training> getTrainingByTrainer(Long trainerId) {
        log.info("Fetching trainings for trainer with id: {}", trainerId);
        List<Training> trainings = dao.getTrainingByTrainer(trainerId);
        log.debug("Found {} trainings for trainer {}", trainings.size(), trainerId);
        return trainings;
    }
}
