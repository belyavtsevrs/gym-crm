package com.epam.gymcore.domain.mapper;

import com.epam.gymcore.domain.dto.TraineeProfileDto;
import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.entity.Trainee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    Trainee toEntity(TraineeRegistrationDto traineeRegistrationDto);

    @Mapping(source = "trainers", target = "trainersList")
    @Mapping(source = "isActive", target = "isActive")
    TraineeProfileDto toProfileDto(Trainee trainee);
}
