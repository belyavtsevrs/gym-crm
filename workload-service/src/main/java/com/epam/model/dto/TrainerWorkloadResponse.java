package com.epam.model.dto;

import java.io.Serializable;
import java.util.List;

public record TrainerWorkloadResponse(
        String username,
        String firstname,
        String lastname,
        Boolean isActive,
        List<YearResponse> years
) implements Serializable {
}
