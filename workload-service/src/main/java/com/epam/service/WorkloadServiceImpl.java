package com.epam.service;

import com.epam.model.dto.MonthResponse;
import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.dto.TrainerWorkloadResponse;
import com.epam.model.dto.YearResponse;
import com.epam.model.entity.Months;
import com.epam.model.entity.TrainerWorkload;
import com.epam.model.entity.Years;
import com.epam.model.mapper.WorkloadReqMapper;
import com.epam.repository.MonthRepository;
import com.epam.repository.WorkloadRepository;
import com.epam.repository.YearRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WorkloadServiceImpl implements WorkloadService {
    private final WorkloadRepository workloadRepository;
    private final MonthRepository monthRepository;
    private final YearRepository yearRepository;

    public WorkloadServiceImpl(WorkloadRepository workloadRepository, MonthRepository monthRepository, YearRepository yearRepository) {
        this.workloadRepository = workloadRepository;
        this.monthRepository = monthRepository;
        this.yearRepository = yearRepository;
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

    public boolean removeWorkloadEvent(TrainerWorkloadRequest wr){
        TrainerWorkload trainer = workloadRepository.findByTrainerUsername(wr.trainerUsername())
                .orElseThrow(() -> new IllegalArgumentException("Trainer not found: " + wr.trainerUsername()));

        Years years = yearRepository.findYearByWorkloadYearAndTrainerWorkload(
                wr.trainingDate().getYear(), trainer
        );
        if (years == null) {
            log.warn("No workload year {} for trainer {}", wr.trainingDate().getYear(), wr.trainerUsername());
            return false;
        }

        Months month = monthRepository.findMonthsByMonthAndYearsRef(
                wr.trainingDate().getMonth(), years
        );
        if (month == null) {
            log.warn("No month {} found for trainer {}", wr.trainingDate().getMonth(), wr.trainerUsername());
            return false;
        }

        long newWorkload = month.getWorkload() - wr.duration().toHours();
        if (newWorkload <= 0) {
            years.getMonths().remove(month);
            monthRepository.delete(month);
            log.info("Removed month {} for trainer {}", wr.trainingDate().getMonth(), wr.trainerUsername());
        } else {
            month.setWorkload(newWorkload);
            monthRepository.save(month);
            log.info("Updated month {} workload for trainer {} → {}", wr.trainingDate().getMonth(), wr.trainerUsername(), newWorkload);
        }

        if (years.getMonths().isEmpty()) {
            trainer.getYears().remove(years);
            yearRepository.delete(years);
            log.info("Removed year {} for trainer {}", wr.trainingDate().getYear(), wr.trainerUsername());
        }

        if (trainer.getYears().isEmpty()) {
            workloadRepository.delete(trainer);
            log.info("Removed trainer workload {} because all years are empty", wr.trainerUsername());
        }

        return true;
    }

    public boolean addWorkloadEvent(TrainerWorkloadRequest wr) {
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

        Years years = trainer.getYears().stream()
                .filter(x -> x.getWorkloadYear() == yearValue)
                .findFirst()
                .orElseGet(() -> {
                    Years newYears = new Years();
                    newYears.setWorkloadYear(yearValue);
                    newYears.setTrainerWorkload(trainer);
                    trainer.getYears().add(newYears);
                    return newYears;
                });

        Months month = years.getMonths().stream()
                .filter(m -> m.getMonth() == monthValue)
                .findFirst()
                .orElseGet(() -> {
                    Months newMonth = new Months(monthValue, 0L);
                    newMonth.setYearsRef(years);
                    years.getMonths().add(newMonth);
                    return newMonth;
                });

        month.setWorkload(month.getWorkload() + durationHours);

        TrainerWorkload saved = workloadRepository.save(trainer);

        log.info("Saved trainerWorkload = {}", saved);
        return true;
    }
}
