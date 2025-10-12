package com.epam.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@ToString
@Entity
public class TrainerWorkload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean isActive;
    @OneToMany(
            mappedBy = "trainerWorkload",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Workload> years = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TrainerWorkload workload)) return false;

        return Objects.equals(trainerUsername, this.trainerUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerUsername);
    }
}
