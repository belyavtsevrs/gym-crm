package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TrainerApi;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trainer/")
public class TrainerController implements TrainerApi {
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Override
    @PostMapping("/register-trainer")
    public ResponseEntity<UserDto> trainerRegistration(TrainerRegistrationDto registrationDto) {
        UserDto registered = trainerService.register(registrationDto);
        return ResponseEntity.ok(registered);
    }

    @Override
    @GetMapping("/login")
    public ResponseEntity<Void> login(String username, String password) {
        trainerService.findByUsernameAndPassword(username,password)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{username}/get-profile")
    public ResponseEntity<TrainerProfileDto> getTrainerProfile(String username) {
        return ResponseEntity.ok(trainerService.getTrainerProfile(username));
    }

    @Override
    @PutMapping("/{username}/update-profile")
    public ResponseEntity<TrainerProfileDto> updateTraineeProfile(String username, TrainerUpdateDto dto) {
        return ResponseEntity.ok(trainerService.updateTrainerProfile(username,dto));
    }

    @Override
    @PutMapping("/{username}/update-login")
    public ResponseEntity<Void> updateLogin(String username, String oldPassword, String newPassword) {
        Trainer trainer = trainerService.findByUsernameAndPassword(username,oldPassword)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        trainerService.updatePasswordByUsername(trainer.getUsername(),newPassword);

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{username}/training-list")
    public ResponseEntity<List<TrainingDto>> getTrainerTrainingList(String username, LocalDateTime from, LocalDateTime to, String traineeName) {
        return ResponseEntity.ok(trainerService.trainerTrainingsList(username,from,to,traineeName));
    }

    @Override
    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus(String username,Boolean isActive) {
        trainerService.changeStatus(username,isActive);
        return ResponseEntity.ok().build();
    }
}
