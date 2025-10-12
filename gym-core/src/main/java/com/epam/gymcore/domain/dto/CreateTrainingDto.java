package com.epam.gymcore.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTrainingDto {
    @NotBlank
    private String trainerName;
    @FutureOrPresent
    private LocalDateTime trainingDate;
    @NotBlank
    private String trainingType;
    private Duration duration;
    @NotBlank
    private String traineeName;
}
