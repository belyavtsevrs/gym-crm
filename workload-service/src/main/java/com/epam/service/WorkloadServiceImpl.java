package com.epam.service;

import com.epam.model.dto.MonthResponse;
import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.dto.TrainerWorkloadResponse;
import com.epam.model.dto.YearResponse;
import com.epam.model.entity.Months;
import com.epam.model.entity.TrainerWorkload;
import com.epam.model.entity.Workload;
import com.epam.model.mapper.WorkloadReqMapper;
import com.epam.repository.WorkloadRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkloadServiceImpl implements WorkloadService {
    private final WorkloadRepository workloadRepository;
    private final WorkloadReqMapper workloadReqMapper;

    public WorkloadServiceImpl(WorkloadRepository workloadRepository,
                               WorkloadReqMapper workloadReqMapper) {
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

    @Override
    public TrainerWorkloadResponse workloadResponse(String username) {
        TrainerWorkload workload = workloadRepository.findByTrainerUsername(username).orElseThrow(()->
                new UsernameNotFoundException(String.format("Wokload for trainer with %s username not found",username)));
        log.info("(workloadResponse) workload = {}",workload);

        List<YearResponse> yearResponses = new ArrayList<>();
        for(var x : workload.getYears()){
            List<MonthResponse> monthResponses = new ArrayList<>();
            for(var y : x.getMonths()){
                monthResponses.add(new MonthResponse(
                        y.getMonth(),
                        y.getWorkload().intValue()
                ));
            }
            yearResponses.add(new YearResponse(
                    x.getWorkloadYear(),
                    monthResponses
            ));
        }
        TrainerWorkloadResponse workloadResponse =
                new TrainerWorkloadResponse(
                        workload.getTrainerUsername(),
                        workload.getTrainerFirstname(),
                        workload.getTrainerLastname(),
                        workload.getIsActive(),
                        yearResponses
                );
        log.info("(TrainerWorkloadResponse) : TrainerWorkloadResponse = {}",workloadResponse);

        return workloadResponse;
    }

    private boolean removeWorkloadEvent(TrainerWorkloadRequest wr){
        return true;
    }

    private boolean addWorkloadEvent(TrainerWorkloadRequest wr) {
        TrainerWorkload trainer = workloadRepository.findByTrainerUsername(wr.trainerUsername())
                .orElseGet(()->{
                    TrainerWorkload newTrainer = new TrainerWorkload();
                    newTrainer.setTrainerFirstname(wr.trainerFirstname());
                    newTrainer.setTrainerLastname(wr.trainerLastname());
                    newTrainer.setTrainerUsername(wr.trainerUsername());
                    newTrainer.setIsActive(wr.isActive());
                    return newTrainer;
                });

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
