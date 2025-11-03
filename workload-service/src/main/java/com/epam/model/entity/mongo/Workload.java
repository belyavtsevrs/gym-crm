package com.epam.model.entity.mongo;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Document(collection = "trainer_workload")
public class Workload {
    @Id
    private String trainerUsername;
    private String trainerFirstname;
    private String trainerLastname;
    private Boolean isActive;
    private List<YearWorkload> years = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Workload workload)) return false;
        return Objects.equals(trainerUsername, workload.trainerUsername)
                && Objects.equals(trainerFirstname, workload.trainerFirstname)
                && Objects.equals(trainerLastname, workload.trainerLastname)
                && Objects.equals(isActive, workload.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainerUsername, trainerFirstname, trainerLastname, isActive);
    }
}
