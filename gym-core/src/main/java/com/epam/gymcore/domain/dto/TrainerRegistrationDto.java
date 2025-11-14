package com.epam.gymcore.domain.dto;

import com.epam.gymcore.domain.entity.TrainingType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerRegistrationDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private Set<TrainingTypeDto> specialization = new HashSet<>();
}
