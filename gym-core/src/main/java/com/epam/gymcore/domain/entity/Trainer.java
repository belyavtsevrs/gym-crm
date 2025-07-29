package com.epam.gymcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "trainers")
public class Trainer extends User {

    @Column(name = "specialization")
    private String specialization;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    public Trainer(String lastName, String firstName, String specialization) {
        super(lastName, firstName);
        this.specialization = specialization;
    }
}
