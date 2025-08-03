package com.epam.gymcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "trainers")
public class Trainer extends User {
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Training> trainings = new HashSet<>();

    @ManyToMany(mappedBy = "trainers")
    private Set<Trainee> trainees = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "trainer_specializations",
            joinColumns = @JoinColumn(name = "trainer_id"),
            inverseJoinColumns = @JoinColumn(name = "training_type_id")
    )
    private Set<TrainingType> specializations = new HashSet<>();

    public Trainer(String firstName, String lastName) {
        super(firstName, lastName);
    }
}
