package com.epam.gymcore.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainingDto {
    @NotBlank
    private String trainingName;
    private LocalDateTime trainingDate;
    @NotBlank
    private String trainingType;
    private Long duration;
    @NotBlank
    private String traineeName;
}
