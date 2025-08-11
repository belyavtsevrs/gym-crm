package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.TraineeProfileDto;
import com.epam.gymcore.domain.dto.TraineeRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface TraineeApi {
    ResponseEntity<UserDto> traineeRegistration(@RequestBody TraineeRegistrationDto registrationDto);
    ResponseEntity<Void> login(@RequestParam("username") String username, @RequestParam("password") String password);
    ResponseEntity<Void> updateLogin(@RequestParam("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword);
    ResponseEntity<TraineeProfileDto> getTraineeProfile(@RequestParam("username") String username);
}
