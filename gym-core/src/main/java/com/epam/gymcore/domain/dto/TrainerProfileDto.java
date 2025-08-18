package com.epam.gymcore.domain.dto;

import java.util.List;

public record TrainerProfileDto(
        String firstName,
        String lastName,
        List<TrainingTypeDto> specializations,
        Boolean isActive,
        List<UserDto> trainees
) {

}
