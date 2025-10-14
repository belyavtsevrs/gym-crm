package com.epam.gymcore.service;

import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.api.Creator;
import com.epam.gymcore.service.api.Retriever;
import com.epam.gymcore.service.api.Updater;

import java.time.LocalDateTime;
import java.util.List;

public interface TrainerService extends
        Creator<Trainer>,
        Updater<Trainer>,
        Retriever<Trainer,Long>,
        UserService<Trainer>
{
    UserDto register(TrainerRegistrationDto dto);
    TrainerProfileDto getTrainerProfile(String username);
    TrainerProfileDto updateTrainerProfile(String username, TrainerUpdateDto updateDto);
    List<TrainingDto> trainerTrainingsList(String username, LocalDateTime from, LocalDateTime to , String traineeName);

}
