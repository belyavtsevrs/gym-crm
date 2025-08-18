package com.epam.gymcore.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerTrainingsDto {
    @NotBlank
    private String username;
    private LocalDateTime from;
    private LocalDateTime to;
    private String traineeName;
}
