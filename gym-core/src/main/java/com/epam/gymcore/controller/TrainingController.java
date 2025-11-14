package com.epam.gymcore.controller;

import com.epam.gymcore.controller.api.TrainingApi;
import com.epam.gymcore.domain.dto.CreateTrainingDto;
import com.epam.gymcore.domain.dto.TrainingTypeDto;
import com.epam.gymcore.service.TrainingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/trainings/")
public class TrainingController implements TrainingApi {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @Override
    @PostMapping("/create-training")
    public ResponseEntity<Void> addTraining(CreateTrainingDto dto) {
        var res = trainingService.createTraining(dto);
        if(res == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }
    // remove by trainer username
    @DeleteMapping("/{username}/delete")
    public ResponseEntity<Void> deleteTraining(@PathVariable("username")String username,@RequestParam("date")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date){
        trainingService.removeTraining(username,date);
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping("/types-of-training")
    public ResponseEntity<List<TrainingTypeDto>> allTypes() {
        return ResponseEntity.ok(trainingService.typesList());
    }

}
