package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TraineeApi;
import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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


}
