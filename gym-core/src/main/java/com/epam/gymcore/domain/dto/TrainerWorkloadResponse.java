package com.epam.gymcore.domain.dto;

import java.util.List;

public record TrainerWorkloadResponse(
        String username,
        String firstname,
        String lastname,
        Boolean isActive,
        List<YearResponse> years
) {
}
