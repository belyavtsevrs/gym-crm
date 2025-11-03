package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TrainerApi;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.TrainerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/trainer/")
public class TrainerController implements TrainerApi {
    private final TrainerService trainerService;
    private final JmsTemplate jmsTemplate;

    public TrainerController(TrainerService trainerService,
                             JmsTemplate jmsTemplate
    ) {
        this.trainerService = trainerService;
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    @PostMapping("/register-trainer")
    public ResponseEntity<UserDto> trainerRegistration(TrainerRegistrationDto registrationDto) {
        UserDto registered = trainerService.register(registrationDto);
        return ResponseEntity.ok(registered);
    }

    @Override
    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(String username, String password) {
            return ResponseEntity.ok(null);

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

    @GetMapping("/{username}/trainer-workload")
    public ResponseEntity<TrainerWorkloadResponse> workloadResponse(
            @PathVariable("username") String username) throws JsonProcessingException, JMSException {
        log.info("Fetching workload for trainer: {}", username);

        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must not be empty");
        }
        jmsTemplate.setReceiveTimeout(5000);
        Message reply = jmsTemplate.sendAndReceive("workload.request.queue",
                session -> session.createTextMessage(username));

        log.info("message = {}",reply);

        String json = ((TextMessage) reply).getText();
        ObjectMapper mapper = new ObjectMapper();
        TrainerWorkloadResponse response = mapper.readValue(json, TrainerWorkloadResponse.class);

        return ResponseEntity.ok(response);
    }

}
