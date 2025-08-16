package com.epam.gymcore.domain.mapper;

import com.epam.gymcore.domain.dto.TraineeProfileDto;
import com.epam.gymcore.domain.dto.TrainerDto;
import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.entity.Trainer;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    Trainer toEntity(TrainerRegistrationDto registrationDto);
    TrainerDto toTrainerDto(Trainer trainer);
    List<TrainerDto> toTrainerDto(Collection<Trainer> trainerSet);
}
