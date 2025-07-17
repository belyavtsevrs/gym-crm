package com.epam.gymcore.domain.model;

import java.time.Duration;
import java.time.LocalDateTime;

public interface Training extends Identifiable<Long> {
    Long getTrainerId();
    Long getTraineeId();
    Long getTrainingTypeId();
    LocalDateTime getTrainingDate();
    Duration getDuration();
}
