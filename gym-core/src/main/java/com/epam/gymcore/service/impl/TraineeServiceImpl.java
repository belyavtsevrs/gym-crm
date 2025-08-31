package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TrainerDao;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.enums.Roles;
import com.epam.gymcore.domain.exception.UserNotFoundException;
import com.epam.gymcore.domain.mapper.TraineeMapper;
import com.epam.gymcore.domain.mapper.TrainerMapper;
import com.epam.gymcore.domain.mapper.TrainingMapper;
import com.epam.gymcore.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TraineeServiceImpl extends AbstractUserService<Trainee> implements TraineeService {
    private final TraineeMapper traineeMapper;
    private final TrainerDao trainerDao;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final PasswordEncoder passwordEncoder;

    protected TraineeServiceImpl(AbstractUserDao<Trainee> dao,
                                 TraineeMapper traineeMapper, TrainerDao trainerDao,
                                 TrainerMapper trainerMapper, TrainingMapper trainingMapper, PasswordEncoder passwordEncoder) {
        super(dao,passwordEncoder);
        this.traineeMapper = traineeMapper;
        this.trainerDao = trainerDao;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDto register(TraineeRegistrationDto dto) {
        Trainee newTrainee = traineeMapper.toEntity(dto);
        newTrainee.setRole(Roles.ROLE_TRAINEE);
        log.info("(TraineeServiceImpl) newTrainee after mapping = {}", newTrainee);

        Trainee saved = super.create(newTrainee);
        log.info("(TraineeServiceImpl) new trainee is registered = {}", saved);

        return new UserDto(saved.getFirstName(),saved.getLastName());
    }

    @Override
    public TraineeProfileDto getTraineeProfile(String username) {
        Trainee trainee = dao.findByUsername(username).orElseThrow(()->
                new UserNotFoundException(String.format("User with username %s not found",username)));

        TraineeProfileDto profile = traineeMapper.toProfileDto(trainee);

        return profile;
    }

    @Override
    public TraineeProfileDto updateTraineeProfile(String username, TraineeUpdateDto updateDto) {
        Trainee trainee = dao.findByUsername(username).orElseThrow(()->
                new UserNotFoundException(String.format("User with username %s not found",username)));

        log.info("(updateTraineeProfile) trainee by username = {}",trainee);

        if (updateDto.getFirstName() != null)   trainee.setFirstName(updateDto.getFirstName());
        if (updateDto.getLastName() != null)    trainee.setLastName(updateDto.getLastName());
        if (updateDto.getDateOfBirth() != null) trainee.setDateOfBirth(updateDto.getDateOfBirth());
        if (updateDto.getAddress() != null)     trainee.setAddress(updateDto.getAddress());
        if (updateDto.getIsActive() != null)    trainee.setIsActive(updateDto.getIsActive());

        Trainee saved = dao.save(trainee);
        log.info("(updateTraineeProfile) after save = {}",saved);

        return traineeMapper.toProfileDto(saved);
    }

    @Override
    public List<TrainerDto> notAssignedTrainers(String username) {
        Trainee trainee = dao.findByUsername(username).orElseThrow(()->
                new UserNotFoundException(String.format("User with username %s not found",username)));

        List<TrainerDto> trainers = trainerMapper.toTrainerDto(trainerDao.findAllActive().stream()
                .filter(x->!trainee.getTrainers().contains(x))
                .collect(Collectors.toSet()));

        return trainers;
    }

    @Override
    public List<TrainerDto> updateTrainerList(String username, List<String> trainers) {
        Trainee trainee = dao.findByUsername(username).orElseThrow(()->
                new UserNotFoundException(String.format("User with username %s not found",username))
        );

        List<Trainer> trainersList = trainers.stream()
                .map(x->trainerDao.findByUsername(x).orElseThrow(()->new UserNotFoundException(x)))
                .toList();

        for(var x : trainersList){
            trainee.getTrainers().add(x);
        }

        dao.save(trainee);
        return trainerMapper.toTrainerDto(trainersList);
    }

    @Override
    public List<TrainingDto> traineeTrainingsList(String username, LocalDateTime from, LocalDateTime to, String traineeName) {
        List<TrainingDto> trainingDtoList = new ArrayList<>();
        List<Training> trainings;
        if(from != null && to != null){
            trainings = dao.getUsersByUsernameAndCriteria(username,from,to);
            log.info("trainings by criteria = {}",trainings);

            for(var x : trainings){
                var trainee = x.getTrainee();
                var type = x.getTrainingType();
                var res = trainingMapper.toDto(x,type,trainee);
                trainingDtoList.add(res);
            }
            if((traineeName != null && !traineeName.isBlank())){
                return trainingDtoList.stream()
                        .filter(x->x.getTraineeName().equals(traineeName))
                        .toList();
            }
            return trainingDtoList;
        }
        Trainee trainee = dao.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(String.format("User with username %s not found", username))
        );
        log.info("trainer = {}",trainee);

        for(var x : trainee.getTrainings()){
            var trainer = x.getTrainee();
            var type = x.getTrainingType();
            var res = trainingMapper.toDto(x,type,trainer);
            trainingDtoList.add(res);
        }
        return trainingDtoList;
    }


    @Override
    public Boolean changeStatus(String username, Boolean isActive) {
        Trainee trainee = dao.findByUsername(username).orElseThrow(()->
                new UserNotFoundException(String.format("User with username %s not found",username))
        );
        trainee.setIsActive(isActive);
        dao.save(trainee);
        return true;
    }
}
