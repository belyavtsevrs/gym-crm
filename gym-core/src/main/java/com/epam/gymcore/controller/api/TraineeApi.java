package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface TraineeApi {
    ResponseEntity<UserDto> traineeRegistration(@RequestBody TraineeRegistrationDto registrationDto);

    ResponseEntity<Void> login(@RequestParam("username") String username, @RequestParam("password") String password);
    ResponseEntity<Void> updateLogin(@RequestParam("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword);

    ResponseEntity<TraineeProfileDto> getTraineeProfile(@RequestParam("username") String username);
    ResponseEntity<TraineeProfileDto> updateTraineeProfile(@RequestParam("username")String username,@RequestBody TraineeUpdateDto dto);
    ResponseEntity<List<TrainerDto>> notAssignedTrainers(@PathVariable("username") String username);
    ResponseEntity<List<TrainerDto>> updateTraineeTrainers(@PathVariable("username")String username,@RequestBody List<String> trainersUsername);

    ResponseEntity<Void> changeStatus();
}
