package com.epam.gymcore.domain.dto;

import java.util.List;

public record TrainerDto(
        String username,
        String firstName,
        String LastName,
        List<TrainingTypeDto> specializations) {
}
