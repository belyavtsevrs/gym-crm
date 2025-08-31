package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TrainerApi;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.security.service.JwtService;
import com.epam.gymcore.security.service.LoginAttemptService;
import com.epam.gymcore.service.TrainerService;
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
@RequestMapping("/api/trainer/")
public class TrainerController implements TrainerApi {
    private final TrainerService trainerService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final LoginAttemptService loginAttemptService;

    public TrainerController(TrainerService trainerService,
                             AuthenticationManager authenticationManager,
                             JwtService jwtService,
                             LoginAttemptService loginAttemptService) {
        this.trainerService = trainerService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.loginAttemptService = loginAttemptService;
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
        if(loginAttemptService.isBlocked(username)){
            return ResponseEntity.ok(new AuthResponse("","amount of attempt is exceded try to login later"));
        }
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtService.generateToken(username);
            loginAttemptService.loginSucceeded(username);
            log.info("jwt = {}",jwt);

            return ResponseEntity.ok(new AuthResponse(jwt));
        }catch (Exception e){
            loginAttemptService.loginFailed(username);
            return ResponseEntity.ok(new AuthResponse("","Login is failed. You have attempts :" + loginAttemptService.getAttempts(username)));
        }
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
