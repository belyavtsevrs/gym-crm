package com.epam.controller;

import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.service.WorkloadService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/workload")
@AllArgsConstructor
public class WorkloadController {
    private final WorkloadService workloadService;

    @PostMapping("/event")
    public ResponseEntity<?> createWorkloadEvent(@RequestBody TrainerWorkloadRequest wr){
        log.info(" Received workload event: {}", wr);
        workloadService.workloadEvent(wr);
        return new ResponseEntity(HttpStatus.OK);
    }
}
