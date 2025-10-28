package com.epam.model.dto;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public record TrainerWorkloadRequest (
        String trainerUsername,
        String trainerFirstname,
        String trainerLastname,
        Boolean isActive,
        LocalDateTime trainingDate,
        Integer duration,
        ActionType actionType
) implements Serializable {
    public enum ActionType{
        ADD,DELETE;
    }
}
