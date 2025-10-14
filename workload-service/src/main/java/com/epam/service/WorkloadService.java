package com.epam.service;

import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.dto.TrainerWorkloadResponse;
import org.springframework.stereotype.Service;

public interface WorkloadService {
    String workloadEvent(TrainerWorkloadRequest wr);
    TrainerWorkloadResponse workloadResponse(String username);
}
