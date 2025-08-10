package com.epam.gymcore.domain.mapper;

import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.entity.Trainee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    Trainee toEntity(TraineeRegistrationDto traineeRegistrationDto);
}
