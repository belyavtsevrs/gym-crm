package com.epam.gymcore.controller.api;

import com.epam.gymcore.domain.dto.TrainingDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface TrainingApi {
    ResponseEntity<Void> addTraining(@RequestBody TrainingDto dto);
}
