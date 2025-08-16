package com.epam.gymcore.domain.dto;

import java.util.List;

public record TrainerDto(
        String username,
        String firstName,
        String lastName,
        List<TrainingTypeDto> specializations
) {
}
