package com.epam.gymcore.client;

import com.epam.gymcore.domain.dto.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "${spring.workload.service.name}",
        url = "${spring.workload.service.url}"
)
public interface WorkloadClient {

    @PostMapping("/workload/event")
    Object createWorkloadEvent(@RequestBody TrainerWorkloadRequest workloadRequest);

}
