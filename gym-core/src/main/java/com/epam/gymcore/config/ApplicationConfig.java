package com.epam.gymcore.config;

import com.epam.gymcore.dao.TrainingTypeDao;
import com.epam.gymcore.domain.dto.CreateTrainingDto;
import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.dto.TrainingTypeDto;
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
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Configuration
public class ApplicationConfig implements CommandLineRunner {
    private final TrainingTypeDao trainingTypeDao;
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;
    public ApplicationConfig(TrainingTypeDao trainingTypeDao,
                             TrainerService trainerService,
                             TraineeService traineeService,
                             TrainingService trainingService) {
        this.trainingTypeDao = trainingTypeDao;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<TrainingType> trainingTypes = List.of(
                new TrainingType("Bodybuilding"),
                new TrainingType("Yoga"),
                new TrainingType("Aerobics"),
                new TrainingType("Dance")
        );
        trainingTypes.forEach(trainingTypeDao::save);

        List<TraineeRegistrationDto> traineeDtos = List.of(
                new TraineeRegistrationDto("Rodion", "B", LocalDate.of(2000, 6, 9), "Biba street 69"),
                new TraineeRegistrationDto("Rodion", "B", LocalDate.of(2001, 8, 14), "Pushkin 96"),
                new TraineeRegistrationDto("Volodya", "Gvozd", LocalDate.of(1999, 6, 9), "Chivapchicheva 148"),
                new TraineeRegistrationDto("Raimbek", "Sayndikov", LocalDate.of(1999, 6, 9), "Raimbeka 148"),
                new TraineeRegistrationDto("Eldar", "Pipa", LocalDate.of(1999, 6, 9), "Pupa 148")
        );

        for (TraineeRegistrationDto dto : traineeDtos) {
            traineeService.register(dto);
        }

        List<Trainee> trainees = traineeService.findAll();

        TrainerRegistrationDto trainer1Dto = new TrainerRegistrationDto();
        trainer1Dto.setFirstName("Vasily");
        trainer1Dto.setLastName("Che");
        trainer1Dto.getSpecialization().add(new TrainingTypeDto("Bodybuilding"));
        trainer1Dto.getSpecialization().add(new TrainingTypeDto("Yoga"));
        trainerService.register(trainer1Dto);

        TrainerRegistrationDto trainer2Dto = new TrainerRegistrationDto();
        trainer2Dto.setFirstName("Batyrbek");
        trainer2Dto.setLastName("Batyrbekovich");
        trainer2Dto.getSpecialization().add(new TrainingTypeDto("Dance"));
        trainerService.register(trainer2Dto);

        List<Trainer> trainers = trainerService.findAll();
        Trainer trainer1 = trainers.stream()
                .filter(t -> t.getFirstName().equals("Vasily"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Trainer1 not found"));
        Trainer trainer2 = trainers.stream()
                .filter(t -> t.getFirstName().equals("Batyrbek"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Trainer2 not found"));

        for (Trainee t : trainees) {
            trainer1.getTrainees().add(t);
            t.getTrainers().add(trainer1);
            traineeService.update(t);
        }

        trainees.get(3).getTrainers().add(trainer2);
        trainer2.getTrainees().add(trainees.get(3));
        traineeService.update(trainees.get(3));

        trainees.get(4).getTrainers().add(trainer2);
        trainer2.getTrainees().add(trainees.get(4));
        traineeService.update(trainees.get(4));

        TrainingType bodybuilding = trainingTypeDao.findByName("Bodybuilding")
                .orElseThrow(() -> new RuntimeException("Bodybuilding type not found"));

        Trainee trainee = trainees.get(0);
        Training training = new Training(
                trainer1,
                trainee,
                bodybuilding,
                LocalDateTime.now(),
                Duration.ofHours(2)
        );

        Stream.of(new Random().nextInt(13)).forEach(i -> {
            CreateTrainingDto dto = new CreateTrainingDto();
            dto.setTraineeName(trainee.getUsername());
            dto.setTrainerName(trainer1.getUsername());
            dto.setTrainingDate(LocalDateTime.now().plusDays(i));
            dto.setTrainingType(bodybuilding.getName());
            dto.setDuration(training.getDuration());
            trainingService.createTraining(dto);
        });

        Stream.of(0, 1, 2,3,4,5,6,7,8,9,10).forEach(i -> {
            CreateTrainingDto dto = new CreateTrainingDto();
            dto.setTraineeName(trainee.getUsername());
            dto.setTrainerName(trainer1.getUsername());
            dto.setTrainingDate(LocalDateTime.now().plusMonths(i));
            dto.setTrainingType(bodybuilding.getName());
            dto.setDuration(training.getDuration());
            trainingService.createTraining(dto);
        });
    }

}
