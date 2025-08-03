package com.epam.gymcore.domain.entity;

import com.epam.gymcore.util.DurationConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor
@ToString
@Table(name = "trainings")
public class Training extends AbstractEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;

    @Column(name = "training_date", nullable = false)
    private LocalDateTime trainingDate;

    @Convert(converter = DurationConverter.class)
    @Column(name = "training_duration", nullable = false)
    private Duration duration;

    public Training(TrainingType trainingType, Duration duration, LocalDateTime trainingDate) {
        this.trainingType = trainingType;
        this.duration = duration;
        this.trainingDate = trainingDate;
    }

    public Training(Trainer trainer, Trainee trainee, TrainingType trainingType, LocalDateTime trainingDate, Duration duration) {
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Training training)) return false;
        return Objects.equals(trainer, training.trainer) && Objects.equals(trainee, training.trainee) && Objects.equals(trainingType, training.trainingType) && Objects.equals(trainingDate, training.trainingDate) && Objects.equals(duration, training.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainer, trainee, trainingType, trainingDate, duration);
    }
}
