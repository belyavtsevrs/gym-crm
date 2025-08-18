package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.CreateTrainingDto;
import com.epam.gymcore.domain.dto.TrainingTypeDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TrainingApi {
    @Operation(summary = "Create training")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "training created"),
            @ApiResponse(responseCode = "400",description = "training not created")
    })
    ResponseEntity<Void> addTraining(@RequestBody CreateTrainingDto dto);

    @Operation(summary = "types of training")
    @ApiResponses({
            @ApiResponse(responseCode = "200",description = "list of types")
    })
    ResponseEntity<List<TrainingTypeDto>> allTypes();
}
