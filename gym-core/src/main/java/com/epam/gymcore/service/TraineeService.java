package com.epam.gymcore.service;

import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.api.*;

import java.time.LocalDateTime;
import java.util.List;

public interface TraineeService extends
        Creator<Trainee>,
        Deleter<Long>,
        Retriever<Trainee,Long>,
        Updater<Trainee>,UserService<Trainee>
{
    UserDto register(TraineeRegistrationDto dto);
    TraineeProfileDto getTraineeProfile(String username);
    TraineeProfileDto updateTraineeProfile(String username, TraineeUpdateDto updateDto);
    List<TrainerDto> notAssignedTrainers(String username);
    List<TrainerDto> updateTrainerList(String username,List<String> trainers);
    List<TrainingDto> traineeTrainingsList(String username, LocalDateTime from, LocalDateTime to , String traineeName);
}
