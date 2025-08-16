package com.epam.gymcore.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public record TraineeProfileDto(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        @JsonProperty("isActive")
        Boolean isActive,
        List<TrainerDto> trainersList
) {
}
