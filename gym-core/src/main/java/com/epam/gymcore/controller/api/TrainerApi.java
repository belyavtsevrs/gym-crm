package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TrainerApi {
    @Operation(summary = "Register a trainer",
            description = "Creates a new trainee profile with generated username and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee registered",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    ResponseEntity<UserDto> trainerRegistration(@RequestBody TrainerRegistrationDto registrationDto);

    @Operation(summary = "Login trainer")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<Void> login(@RequestParam("username") String username,@RequestParam("password") String password);

    @Operation(summary = "Get trainee profile")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TrainerProfileDto.class))))
    ResponseEntity<TrainerProfileDto> getTrainerProfile(@PathVariable("username") String username);

    @Operation(summary = "Update trainee profile")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = TrainerProfileDto.class))))
    ResponseEntity<TrainerProfileDto> updateTraineeProfile(@PathVariable("username")String username,@RequestBody TrainerUpdateDto dto);

    @Operation(summary = "Change trainer password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed"),
            @ApiResponse(responseCode = "401", description = "Old password mismatch")
    })
    ResponseEntity<Void> updateLogin(@PathVariable("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword);

    @Operation(summary = "Get training list")
    ResponseEntity<List<TrainingDto>> getTrainerTrainingList(
            @PathVariable("username") String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String traineeName
    );

    @Operation(summary = "Activate / Deactivate trainer (non-idempotent)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Status changed"))
    ResponseEntity<Void> changeStatus(@RequestParam("username") String username, @RequestParam("isActive") Boolean isActive);
}
