package com.epam.gymcore.domain.entity;

import com.epam.gymcore.domain.model.Trainee;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TraineeImpl extends UserImpl implements Trainee {
    private LocalDate dateBirth;
    private String address;
    private Long userId;

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public void setId(Long aLong) {
        this.userId = aLong;
    }

    public TraineeImpl(String firstName, String lastName, String username, String password, boolean isActive, LocalDate dateBirth, String address) {
        super(firstName, lastName, username, password, isActive);
        this.dateBirth = dateBirth;
        this.address = address;
    }

    @JsonCreator
    public TraineeImpl(
            @JsonProperty("userId") Long userId,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("isActive") boolean isActive,
            @JsonProperty("dateBirth") LocalDate dateBirthStr,
            @JsonProperty("address") String address) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.userId = userId;
        this.dateBirth = dateBirthStr;
        this.address = address;
    }
}
