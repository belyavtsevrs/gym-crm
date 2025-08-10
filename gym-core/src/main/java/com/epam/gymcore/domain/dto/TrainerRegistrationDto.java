package com.epam.gymcore.domain.dto;

import com.epam.gymcore.domain.entity.TrainingType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerRegistrationDto {
    private String firstName;
    private String lastName;
    private Set<TrainingTypeDto> specialization = new HashSet<>();
}
