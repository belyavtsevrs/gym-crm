package com.epam.gymcore.service;

import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.api.Creator;
import com.epam.gymcore.service.api.Retriever;
import com.epam.gymcore.service.api.Updater;

public interface TrainerService extends
        Creator<Trainer>,
        Updater<Trainer>,
        Retriever<Trainer,Long>,
        UserService<Trainer>
{
    UserDto register(TrainerRegistrationDto dto);
}
