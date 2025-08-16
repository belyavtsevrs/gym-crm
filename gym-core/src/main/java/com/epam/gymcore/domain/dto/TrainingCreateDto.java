package com.epam.gymcore.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainingCreateDto {
    private String traineeUsername;
    private String trainerUsername;
    private String trainingName;
    private LocalDateTime trainingDate;
    private Long duration;
}
