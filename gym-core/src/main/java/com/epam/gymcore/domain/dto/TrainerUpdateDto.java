package com.epam.gymcore.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public  record TrainerUpdateDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull  Boolean isActive
) {

}