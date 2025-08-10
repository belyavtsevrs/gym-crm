package com.epam.gymcore.domain.mapper;

import com.epam.gymcore.domain.dto.TrainingTypeDto;
import com.epam.gymcore.domain.entity.TrainingType;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface TrainingTypeMapper {
    TrainingType toEntity(TrainingTypeDto typeDto);
    Set<TrainingType> toEntitiesSet(Set<TrainingTypeDto> trainingTypeDtoSet);
}
