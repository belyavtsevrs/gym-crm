package com.epam.service;

import com.epam.model.dto.TrainerWorkloadRequest;
import org.springframework.stereotype.Service;

public interface WorkloadService {
    String workloadEvent(TrainerWorkloadRequest wr);
}
