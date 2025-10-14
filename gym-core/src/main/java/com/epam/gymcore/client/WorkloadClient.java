package com.epam.gymcore.client;

import com.epam.gymcore.domain.dto.TrainerWorkloadRequest;
import com.epam.gymcore.domain.dto.TrainerWorkloadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "${spring.workload.service.name}",
        url = "${spring.workload.service.url}",
        path = "/workload"
)
public interface WorkloadClient {

    @PostMapping("/event")
    Object createWorkloadEvent(@RequestBody TrainerWorkloadRequest workloadRequest);

    @GetMapping("/{username}/trainer-workload")
    TrainerWorkloadResponse trainerWorkload(@PathVariable("username")String username);
}
