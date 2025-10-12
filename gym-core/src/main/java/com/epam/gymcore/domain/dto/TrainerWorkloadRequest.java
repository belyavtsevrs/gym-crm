package com.epam.gymcore.domain.dto;

import java.time.Duration;
import java.time.LocalDateTime;

public record TrainerWorkloadRequest (
        String trainerUsername,
        String trainerFirstname,
        String trainerLastname,
        Boolean isActive,
        LocalDateTime trainingDate,
        Duration duration,
        ActionType actionType
){
    public enum ActionType{
        ADD,DELETE;
    }
}
