package com.epam.gymcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "trainees")
@PrimaryKeyJoinColumn(name = "user_id")
@NoArgsConstructor
public class Trainee extends User {
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    public Trainee(String lastName, String firstName, LocalDate dateOfBirth, String address) {
        super(lastName, firstName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }
}
