package com.epam.gymcore.domain.mapper;

import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.entity.Trainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    Trainer toEntity(TrainerRegistrationDto registrationDto);
}
