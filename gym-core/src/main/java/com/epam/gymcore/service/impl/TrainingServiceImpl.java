package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.TrainingDao;
import com.epam.gymcore.dao.TrainingTypeDao;
import com.epam.gymcore.domain.dto.TrainingDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.TrainingType;
import com.epam.gymcore.domain.exception.UserNotFoundException;
import com.epam.gymcore.domain.mapper.TrainingMapper;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.TrainerService;
import com.epam.gymcore.service.TrainingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao trainingDao;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingMapper trainingMapper;

    public TrainingServiceImpl(TrainingDao trainingDao, TraineeService traineeService, TrainerService trainerService, TrainingTypeDao trainingTypeDao, TrainingMapper trainingMapper) {
        this.trainingDao = trainingDao;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public List<Training> getTrainingByTrainee(Long traineeId) {
        return trainingDao.getTrainingByTrainee(traineeId);
    }

    @Override
    public List<Training> getTrainingByTrainer(Long trainerId) {
        return trainingDao.getTrainingByTrainee(trainerId);
    }

    @Override
    public TrainingDto createTraining(TrainingDto trainingDto) {
        Trainee trainee = traineeService.findByUsername(trainingDto.getTraineeName()).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );

        Trainer trainer = trainerService.findByUsername(trainingDto.getTraineeName()).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );

        TrainingType type = trainingTypeDao.findByName(trainingDto.getTraineeName()).get();

        Training newTraining = new Training
                (trainer,trainee,type,trainingDto.getTrainingDate(),Duration.ofHours(trainingDto.getDuration()));

        Training save = create(newTraining);
        log.info("training after save = {}",save);

        return trainingMapper.toDto(save,save.getTrainingType(),save.getTrainee());
    }

    @Override
    public Training create(Training entity) {
        return trainingDao.save(entity);
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
