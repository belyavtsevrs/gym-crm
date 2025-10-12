package com.epam.service;

import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.entity.Months;
import com.epam.model.entity.TrainerWorkload;
import com.epam.model.entity.Workload;
import com.epam.model.mapper.WorkloadReqMapper;
import com.epam.repository.WorkloadRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Month;

@Slf4j
@Service
public class WorkloadServiceImpl implements WorkloadService {
    private final WorkloadRepository workloadRepository;
    private final WorkloadReqMapper workloadReqMapper;

    public WorkloadServiceImpl(WorkloadRepository workloadRepository, WorkloadReqMapper workloadReqMapper) {
        this.workloadRepository = workloadRepository;
        this.workloadReqMapper = workloadReqMapper;
    }

    @Override
    @Transactional
    public String workloadEvent(TrainerWorkloadRequest wr) {
        log.info("(workloadEvent) new event = {}",wr);

        switch (wr.actionType()){
            case ADD -> addWorkloadEvent(wr);
            case DELETE -> removeWorkloadEvent(wr);
        }

        return wr.actionType().name();
    }

    private boolean removeWorkloadEvent(TrainerWorkloadRequest wr){

        return true;
    }

    private boolean addWorkloadEvent(TrainerWorkloadRequest wr) {
        TrainerWorkload trainer = new TrainerWorkload();
        trainer.setTrainerFirstname(wr.trainerFirstname());
        trainer.setTrainerLastname(wr.trainerLastname());
        trainer.setTrainerUsername(wr.trainerUsername());
        trainer.setIsActive(wr.isActive());

        int yearValue = wr.trainingDate().getYear();
        Month monthValue = wr.trainingDate().getMonth();
        long durationHours = wr.duration().toHours();

        Workload year = trainer.getYears().stream()
                .filter(x -> x.getWorkloadYear() == yearValue)
                .findFirst()
                .orElseGet(() -> {
                    Workload newYear = new Workload();
                    newYear.setWorkloadYear(yearValue);
                    newYear.setTrainerWorkload(trainer);
                    trainer.getYears().add(newYear);
                    return newYear;
                });

        Months month = year.getMonths().stream()
                .filter(m -> m.getMonth() == monthValue)
                .findFirst()
                .orElseGet(() -> {
                    Months newMonth = new Months(monthValue, 0L);
                    newMonth.setWorkloadRef(year);
                    year.getMonths().add(newMonth);
                    return newMonth;
                });

        month.setWorkload(month.getWorkload() + durationHours);

        TrainerWorkload saved = workloadRepository.save(trainer);

        log.info("Saved trainerWorkload = {}", saved);
        return true;
    }
}
