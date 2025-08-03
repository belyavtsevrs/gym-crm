package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.TrainingDao;
import com.epam.gymcore.dao.TrainingTypeDto;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.service.TrainingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao trainingDao;

    public TrainingServiceImpl(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    public List<Training> getTrainingByTrainee(Long traineeId) {
        return List.of();
    }

    @Override
    public List<Training> getTrainingByTrainer(Long trainerId) {
        return List.of();
    }

    @Override
    public Training create(Training entity) {
        return trainingDao.save(entity).get();
    }

    @Override
    public List<Training> findAll() {
        return trainingDao.findAll();
    }

    @Override
    public Optional<Training> findById(Long aLong) {
        return trainingDao.findById(aLong);
    }
}
