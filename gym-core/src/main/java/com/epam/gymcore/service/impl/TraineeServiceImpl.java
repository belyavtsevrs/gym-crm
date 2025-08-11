package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.domain.dto.TraineeProfileDto;
import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.dto.TrainerDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.mapper.TraineeMapper;
import com.epam.gymcore.domain.mapper.TrainerMapper;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.UserService;
import com.epam.gymcore.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TraineeServiceImpl extends AbstractUserService<Trainee> implements TraineeService {
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    protected TraineeServiceImpl(AbstractUserDao<Trainee> dao, TraineeMapper traineeMapper, TrainerMapper trainerMapper) {
        super(dao);
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
    }


    @Override
    public UserDto register(TraineeRegistrationDto dto) {
        Trainee newTrainee = traineeMapper.toEntity(dto);
        log.info("(TraineeServiceImpl) newTrainee after mapping = {}", newTrainee);

        Trainee saved = super.create(newTrainee);
        log.info("(TraineeServiceImpl) new trainee is registered = {}", saved);

        return new UserDto(saved.getFirstName(),saved.getLastName());
    }

    @Override
    public TraineeProfileDto getTraineeProfile(String username) {
        Trainee trainee = dao.findByUsername(username).orElseThrow(()->
                new RuntimeException(String.format("User with username %s not found",username)));

        TraineeProfileDto profile = traineeMapper.toProfileDto(trainee);

        return profile;
    }
}
