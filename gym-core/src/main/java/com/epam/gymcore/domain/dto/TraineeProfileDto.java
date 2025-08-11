package com.epam.gymcore.domain.dto;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        boolean isActive,
        List<TrainerDto> trainersList
) {
}
