package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.mapper.TraineeMapper;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.UserService;
import com.epam.gymcore.util.UserUtil;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl extends AbstractUserService<Trainee> implements TraineeService {
    private final TraineeMapper traineeMapper;
    protected TraineeServiceImpl(AbstractUserDao<Trainee> dao, TraineeMapper traineeMapper) {
        super(dao);
        this.traineeMapper = traineeMapper;
    }


    @Override
    public UserDto register(TraineeRegistrationDto dto) {
        Trainee newTrainee = traineeMapper.toEntity(dto);
        log.info("(TraineeServiceImpl) newTrainee after mapping = {}", newTrainee);

        Trainee saved = super.create(newTrainee);
        log.info("(TraineeServiceImpl) new trainee is registered = {}", saved);

        return new UserDto(saved.getFirstName(),saved.getLastName());
    }
}
