package com.epam.controller;

import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.dto.TrainerWorkloadResponse;
import com.epam.service.WorkloadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/workload")
public class WorkloadController {
    private final WorkloadService workloadService;

    public WorkloadController(@Qualifier(value = "workloadServiceImpl") WorkloadService workloadService) {
        this.workloadService = workloadService;
    }

    @PostMapping("/event")
    public ResponseEntity<?> createWorkloadEvent(@RequestBody TrainerWorkloadRequest wr){
        log.info("Received workload event: {}", wr);
        workloadService.workloadEvent(wr);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{username}/trainer-workload")
    public ResponseEntity<TrainerWorkloadResponse> trainerWorkload(@PathVariable("username") String username){
        TrainerWorkloadResponse response = workloadService.workloadResponse(username);
        return ResponseEntity.ok(response);
    }
}
