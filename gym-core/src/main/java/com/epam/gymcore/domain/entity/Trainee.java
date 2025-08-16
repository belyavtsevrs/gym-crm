package com.epam.gymcore.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private Set<Training> trainings = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers = new HashSet<>();

    public Trainee(String firstName,String lastName,LocalDate dateOfBirth, String address) {
        super(firstName,lastName);
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Trainee trainee)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(dateOfBirth, trainee.dateOfBirth) && Objects.equals(address, trainee.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dateOfBirth, address);
    }
}
