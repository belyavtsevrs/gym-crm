package com.epam.gymcore.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraineeRegistrationDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
}
