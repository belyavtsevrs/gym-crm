package com.epam.gymcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "training_types")
public class TrainingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "training_type_name", unique = true, nullable = false)
    private String name;

    public TrainingType(String name) {
        this.name = name;
    }
}
