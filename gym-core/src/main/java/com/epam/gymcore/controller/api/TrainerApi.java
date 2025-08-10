package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface TrainerApi {
    ResponseEntity<UserDto> trainerRegistration(@RequestBody TrainerRegistrationDto registrationDto);
    ResponseEntity<Void> login(@RequestParam("username") String username,@RequestParam("password") String password);
    ResponseEntity<Void> updateLogin(@RequestParam("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword);

}
