package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TraineeApi;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/trainee/")
public class TraineeController implements TraineeApi {
    private final TraineeService traineeService;

    public TraineeController(TraineeService traineeService) {
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
    public ResponseEntity<Void> login(String username, String password) {
        traineeService.findByUsernameAndPassword(username,password)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        return ResponseEntity.ok().build();
    }

    @Override
    @PutMapping("/update-login")
    public ResponseEntity<Void> updateLogin(String username, String oldPassword, String newPassword) {
        Trainee trainee = traineeService.findByUsernameAndPassword(username,oldPassword)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        traineeService.updatePasswordByUsername(trainee.getUsername(),newPassword);

        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/get-profile")
    public ResponseEntity<TraineeProfileDto> getTraineeProfile(String username) {
        return ResponseEntity.ok(traineeService.getTraineeProfile(username));
    }

    @Override
    @PutMapping("/update-profile")
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
    @PatchMapping("/change-status")
    public ResponseEntity<Void> changeStatus() {
        return null;
    }


}
