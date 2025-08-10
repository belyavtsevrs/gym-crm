package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TrainerApi;
import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.TrainerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PutMapping("/update-login")
    public ResponseEntity<Void> updateLogin(String username, String oldPassword, String newPassword) {
        Trainer trainer = trainerService.findByUsernameAndPassword(username,oldPassword)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        trainerService.updatePasswordByUsername(trainer.getUsername(),newPassword);

        return ResponseEntity.ok().build();
    }


}
