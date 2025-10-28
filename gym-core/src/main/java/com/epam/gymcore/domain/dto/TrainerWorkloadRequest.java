package com.epam.gymcore.domain.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public record TrainerWorkloadRequest (
        String trainerUsername,
        String trainerFirstname,
        String trainerLastname,
        Boolean isActive,
        LocalDateTime trainingDate,
        Long duration,
        ActionType actionType
) implements Serializable {
    public enum ActionType{
        ADD,DELETE;
    }
}
