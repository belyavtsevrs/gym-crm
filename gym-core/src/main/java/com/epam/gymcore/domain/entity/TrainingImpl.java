package com.epam.gymcore.domain.entity;

import com.epam.gymcore.domain.model.Training;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class TrainingImpl implements Training {
    private Long id;
    private Long trainerId;
    private Long traineeId;
    private Long trainingTypeId;
    private LocalDateTime trainingDate;
    private Duration duration;

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    @JsonCreator
    public TrainingImpl(
            @JsonProperty("id") Long id,
            @JsonProperty("trainerId") Long trainerId,
            @JsonProperty("traineeId") Long traineeId,
            @JsonProperty("trainingTypeId") Long trainingTypeId,
            @JsonProperty("trainingDate") LocalDateTime trainingDate,
            @JsonProperty("duration") Duration duration
    ) {
        this.id = id;
        this.trainerId = trainerId;
        this.traineeId = traineeId;
        this.trainingTypeId = trainingTypeId;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }
}
