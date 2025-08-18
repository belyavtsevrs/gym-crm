package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Trainees", description = "Trainee profile & trainings")
public interface TraineeApi {
    @Operation(summary = "Register a trainee",
            description = "Creates a new trainee profile with generated username and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Trainee registered",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    ResponseEntity<UserDto> traineeRegistration(@RequestBody TraineeRegistrationDto registrationDto);

    @Operation(summary = "Login trainee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    ResponseEntity<Void> login(@RequestParam("username") String username, @RequestParam("password") String password);

    @Operation(summary = "Change trainee password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed"),
            @ApiResponse(responseCode = "401", description = "Old password mismatch")
    })
    ResponseEntity<Void> updateLogin(@PathVariable("username") String username, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword);

    @Operation(summary = "Get trainee profile")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = TraineeProfileDto.class))))
    ResponseEntity<TraineeProfileDto> getTraineeProfile(@PathVariable("username") String username);

    @Operation(summary = "Update trainee profile")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Updated",
            content = @Content(schema = @Schema(implementation = TraineeProfileDto.class))))
    ResponseEntity<TraineeProfileDto> updateTraineeProfile(@PathVariable("username")String username,@RequestBody TraineeUpdateDto dto);

    @Operation(summary = "Get active trainers not assigned to trainee")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "OK"))
    ResponseEntity<List<TrainerDto>> notAssignedTrainers(@PathVariable("username") String username);

    @Operation(summary = "Replace trainee's trainer list")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Updated"))
    ResponseEntity<List<TrainerDto>> updateTraineeTrainers(@PathVariable("username")String username,@RequestBody List<String> trainersUsername);

    @Operation(summary = "Get training list")
    ResponseEntity<List<TrainingDto>> getTraineeTrainingList(
            @PathVariable("username") String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(required = false) String traineeName
    );

    @Operation(summary = "Delete trainee profile (hard delete)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Deleted"))
    ResponseEntity<Void> deleteTrainee(@RequestParam("username")String username);

    @Operation(summary = "Activate / Deactivate trainee (non-idempotent)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Status changed"))
    ResponseEntity<Void> changeStatus(String username,Boolean isActive);
}
