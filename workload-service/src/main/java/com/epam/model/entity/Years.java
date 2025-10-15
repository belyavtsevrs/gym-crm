package com.epam.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@ToString(exclude = "trainerWorkload")
@AllArgsConstructor
@NoArgsConstructor
public class Years {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Integer workloadYear;
    @OneToMany(targetEntity = Months.class,cascade = CascadeType.ALL)
    private List<Months> months = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private TrainerWorkload trainerWorkload;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Years years)) return false;
        return Objects.equals(id, years.id)
                && Objects.equals(workloadYear, years.workloadYear)
                && Objects.equals(trainerWorkload, years.trainerWorkload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workloadYear, trainerWorkload);
    }
}
