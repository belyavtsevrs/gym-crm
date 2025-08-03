package com.epam.gymcore.config;

import com.epam.gymcore.dao.TrainingTypeDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.TrainingType;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.TrainerService;
import com.epam.gymcore.service.TrainingService;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class ApplicationConfig implements CommandLineRunner {
    private final TrainingTypeDto trainingTypeDto;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    public ApplicationConfig(TrainingTypeDto trainingTypeDto, TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainingTypeDto = trainingTypeDto;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<TrainingType> trainingTypes =
                List.of(
                new TrainingType("Bodybuilding"),
                new TrainingType("Yoga"),
                new TrainingType("Aerobics"),
                new TrainingType("Dance")
        );

        for (TrainingType type : trainingTypes) {
            trainingTypeDto.save(type);
        }

        List<Trainee> trainees =
                List.of(
                new Trainee("Rodion", "B", LocalDate.of(2000, 6, 9), "Biba street 69"),
                new Trainee("Rodion", "B", LocalDate.of(2001, 8, 14), "Pushkin 96"),
                new Trainee("Volodya", "Gvozd", LocalDate.of(1999, 6, 9), "Chivapchicheva 148"),
                new Trainee("Raimbek", "Sayndikov", LocalDate.of(1999, 6, 9), "Raimbeka 148"),
                new Trainee("Eldar", "Pipa", LocalDate.of(1999, 6, 9), "pupa 148")
        );

        for (Trainee trainee : trainees) {
            traineeService.create(trainee);
        }

        Trainer trainer1 = new Trainer("Vasily", "Che");
        trainer1.getSpecializations().add(trainingTypes.get(0));
        trainer1.getSpecializations().add(trainingTypes.get(1));
        trainerService.create(trainer1);

        Trainer trainer2 = new Trainer("Batyrbek", "Batyrbekovich");
        trainer2.getSpecializations().add(trainingTypes.get(3));
        trainerService.create(trainer2);

        for (Trainee trainee : trainees) {
            trainer1.getTrainees().add(trainee);
            trainee.getTrainers().add(trainer1);
            traineeService.update(trainee);
        }

        trainees.get(3).getTrainers().add(trainer2);
        trainer2.getTrainees().add(trainees.get(3));
        traineeService.update(trainees.get(3));

        trainees.get(4).getTrainers().add(trainer2);
        trainer2.getTrainees().add(trainees.get(4));
        traineeService.update(trainees.get(4));

        TrainingType Bodybuilding = trainingTypeDto.findByTitle("Bodybuilding").get();

        Trainee trainee = trainees.get(0);
        Training training =
                new Training(trainer1,trainee,Bodybuilding,LocalDateTime.now(), Duration.ofHours(2));

        trainingService.create(training);
    }

}
