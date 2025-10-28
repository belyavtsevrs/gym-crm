package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.TrainingDao;
import com.epam.gymcore.dao.TrainingTypeDao;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.TrainingType;
import com.epam.gymcore.domain.exception.UserNotFoundException;
import com.epam.gymcore.domain.mapper.TrainingMapper;
import com.epam.gymcore.domain.mapper.TrainingTypeMapper;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.TrainerService;
import com.epam.gymcore.service.TrainingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final TrainingTypeMapper trainingTypeMapper;
    private final JmsTemplate jmsTemplate;
    public TrainingServiceImpl(TrainingDao trainingDao,
                               TraineeService traineeService,
                               TrainerService trainerService,
                               TrainingTypeDao trainingTypeDao,
                               TrainingMapper trainingMapper,
                               TrainingTypeMapper trainingTypeMapper,  JmsTemplate jmsTemplate) {
        this.trainingDao = trainingDao;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingMapper = trainingMapper;
        this.trainingTypeMapper = trainingTypeMapper;
        this.jmsTemplate = jmsTemplate;
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
    @Transactional
    @CircuitBreaker(name = "workloadService", fallbackMethod = "createTrainingEvent")
    public TrainingDto createTraining(CreateTrainingDto trainingDto) {
        Trainee trainee = traineeService.findByUsername(trainingDto.getTraineeName()).orElseThrow(
                ()-> new UserNotFoundException("User not found" + trainingDto.getTraineeName())
        );
        log.info("trainee = {}",trainee);
        Trainer trainer = trainerService.findByUsername(trainingDto.getTrainerName()).orElseThrow(
                ()-> new UserNotFoundException("User not found")
        );
        log.info("trainer = {}",trainer);

        TrainingType type = trainingTypeDao.findByName(trainingDto.getTrainingType()).get();
        log.info("type = {}",type);

        Training newTraining = new Training
                (trainer,trainee,type,trainingDto.getTrainingDate(),trainingDto.getDuration());

        Training save = trainingDao.save(newTraining);
        log.info("training after save = {}",save);

        var workloadRequest = new TrainerWorkloadRequest(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                trainer.getIsActive(),
                newTraining.getTrainingDate(),
                newTraining.getDuration().toHours(),
                TrainerWorkloadRequest.ActionType.ADD);
        log.info("training request = {}",workloadRequest);
        jmsTemplate.convertAndSend("workloadEvent.queue",workloadRequest);
        
        return trainingMapper.toDto(save,save.getTrainingType(),save.getTrainee());
    }

    @Override
    public List<TrainingTypeDto> typesList() {
        return trainingTypeMapper.toDto(trainingTypeDao.findAll());
    }

    @Override
    @CircuitBreaker(name = "workloadService", fallbackMethod = "removeTrainingFallback")
    public boolean removeTraining(String username, LocalDateTime dateTime) {
        try {
            Training training = trainingDao.findTrainingByTrainerAndDate(username,dateTime).orElseThrow(()->
                    new RuntimeException("training not found"));

            var workloadRequest = new TrainerWorkloadRequest(
                    training.getTrainer().getUsername(),
                    training.getTrainer().getFirstName(),
                    training.getTrainer().getLastName(),
                    training.getTrainer().getIsActive(),
                    training.getTrainingDate(),
                    training.getDuration().toHours(),
                    TrainerWorkloadRequest.ActionType.DELETE);
            log.info("workload request = {}",workloadRequest);

            log.info("Sending workload DELETE event: {}", workloadRequest);
            jmsTemplate.convertAndSend("workload.queue",workloadRequest);

            trainingDao.remove(training.getId());
            log.info("Training for trainer '{}' at '{}' removed successfully", username, dateTime);
            return true;

        }catch (Exception e){
            log.error("Error removing training for username {} and date {}: {}", username, dateTime, e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
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

    private TrainingDto createTrainingEvent(CreateTrainingDto dto, Throwable t) {
        log.warn("Fallback triggered for createTraining(): {}", t.getMessage());
        return new TrainingDto();
    }

    private boolean removeTrainingFallback(String username, LocalDateTime dateTime, Throwable t) {
        log.warn("Fallback triggered for removeTraining(): {}", t.getMessage());
        return false;
    }
}
