package com.epam.gymcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "training_types")
@NoArgsConstructor
public class TrainingType extends AbstractEntity{

    @Column(name = "training_type_name", unique = true, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "specializations")
    private Set<Trainer> trainers = new HashSet<>();

    public TrainingType(String name) {
        this.name = name;
    }
}
