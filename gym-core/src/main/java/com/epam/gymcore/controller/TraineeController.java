package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TraineeApi;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/trainee/")
public class TraineeController implements TraineeApi {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService ) {
        this.traineeService = traineeService;
    }

    @Override
    @PostMapping("/register-trainee")
    public ResponseEntity<UserDto> traineeRegistration(TraineeRegistrationDto registrationDto) {
        UserDto registered = traineeService.register(registrationDto);
        return ResponseEntity.ok(registered);
    }

    @Override
    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(String username, String password) {
       return null;
    }

    @Override
    @PutMapping("/{username}/update-login")
    public ResponseEntity<Void> updateLogin(String username, String oldPassword, String newPassword) {
        Trainee trainee = traineeService.findByUsernameAndPassword(username,oldPassword)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        traineeService.updatePasswordByUsername(trainee.getUsername(),newPassword);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/{username}/get-profile")
    public ResponseEntity<TraineeProfileDto> getTraineeProfile(String username) {
        return ResponseEntity.ok(traineeService.getTraineeProfile(username));
    }

    @Override
    @PutMapping("/{username}/update-profile")
    public ResponseEntity<TraineeProfileDto> updateTraineeProfile(String username, TraineeUpdateDto dto) {
        return ResponseEntity.ok(traineeService.updateTraineeProfile(username,dto));
    }

    @Override
    @GetMapping("/{username}/available-trainers")
    public ResponseEntity<List<TrainerDto>> notAssignedTrainers(String username) {
        return ResponseEntity.ok(traineeService.notAssignedTrainers(username));
    }

    @Override
    @PutMapping("/{username}/update-trainee-trainers")
    public ResponseEntity<List<TrainerDto>> updateTraineeTrainers(String username, List<String> trainersUsername) {
        return ResponseEntity.ok(traineeService.updateTrainerList(username,trainersUsername));
    }

    @Override
    @GetMapping("/{username}/training-list")
    public ResponseEntity<List<TrainingDto>> getTraineeTrainingList(String username, LocalDateTime from, LocalDateTime to, String traineeName) {
        return ResponseEntity.ok(traineeService.traineeTrainingsList(username,from,to,traineeName));
    }

    @Override
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTrainee(String username) {
        traineeService.deleteByUsername(username);
        return ResponseEntity.ok().build();
    }

    @Override
    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus(String username,Boolean isActive) {
        traineeService.changeStatus(username,isActive);
        return ResponseEntity.ok().build();
    }

}
