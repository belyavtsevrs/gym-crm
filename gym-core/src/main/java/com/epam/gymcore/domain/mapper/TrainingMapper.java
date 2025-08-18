package com.epam.gymcore.domain.mapper;

import com.epam.gymcore.domain.dto.CreateTrainingDto;
import com.epam.gymcore.domain.dto.TrainingDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.TrainingType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;


@Mapper(componentModel = "spring")
public interface TrainingMapper {
    @Mapping(target = "trainingName", source = "type.name")
    @Mapping(target = "trainingDate", source = "training.trainingDate")
    @Mapping(target = "trainingType", source = "type.name")
    @Mapping(target = "duration",     source = "training.duration")
    @Mapping(target = "traineeName",  source = "trainee.username")
    TrainingDto toDto(Training training, TrainingType type,Trainee trainee);

    default long map(Duration duration) {
        return duration == null ? 0L : duration.toMinutes();
    }
}
