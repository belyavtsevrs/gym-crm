package com.epam.gymcore.domain.entity;

import com.epam.gymcore.domain.model.Trainer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TrainerImpl extends UserImpl implements Trainer {
    private String specialization;
    private Long userId;

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public void setId(Long aLong) {
        this.userId = aLong;
    }

    public TrainerImpl(String firstName, String lastName, String username, String password, boolean isActive, String specialization) {
        super(firstName, lastName, username, password, isActive);
        this.specialization = specialization;
    }

    @JsonCreator
    public TrainerImpl(
            @JsonProperty("userId") Long userId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("specialization") String specialization) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.userId = userId;
        this.specialization = specialization;
    }
}
