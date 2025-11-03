package com.epam.service;

import com.epam.model.dto.MonthResponse;
import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.dto.TrainerWorkloadResponse;
import com.epam.model.dto.YearResponse;
import com.epam.model.entity.Months;
import com.epam.model.entity.TrainerWorkload;
import com.epam.model.entity.Years;
import com.epam.model.entity.mongo.MonthWorkload;
import com.epam.model.entity.mongo.Workload;
import com.epam.model.entity.mongo.YearWorkload;
import com.epam.repository.MongoWorkloadRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service(value = "mongoWorkloadService")
public class MongoWorkloadService implements WorkloadService{
    private final MongoWorkloadRepository workloadRepository;

    public MongoWorkloadService(MongoWorkloadRepository workloadRepository) {
        this.workloadRepository = workloadRepository;
    }

    @Override
    @SendTo("workloadEvent.response.queue")
    @JmsListener(destination = "workloadEvent.queue")
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
        Workload workload = workloadRepository.findByTrainerUsername(username).orElseThrow(()->
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
        Workload workload = workloadRepository.findByTrainerUsername(wr.trainerUsername())
                .orElseThrow(()->new UsernameNotFoundException("user" + wr.trainerUsername() + "not found"));

        Integer year = wr.trainingDate().getYear();

        YearWorkload yearWorkload = workload.getYears().stream()
                .filter(x->x.getWorkloadYear().equals(year))
                .findAny().orElseThrow(()->new RuntimeException("training with such date not found"));

        MonthWorkload monthWorkload = yearWorkload.getMonths().stream()
                .filter(m -> m.getMonth().equals(wr.trainingDate().getMonth()))
                .findAny().orElseThrow(()->new RuntimeException("training with such date not found"));

        monthWorkload.setWorkload(monthWorkload.getWorkload().longValue() - wr.duration());
        if (monthWorkload.getWorkload() == 0) {
            yearWorkload.getMonths().remove(monthWorkload);
        }

        if (yearWorkload.getMonths().isEmpty()) {
            workload.getYears().remove(yearWorkload);
        }

        workloadRepository.save(workload);

        return true;
    }

    public boolean addWorkloadEvent(TrainerWorkloadRequest wr) {
        Workload workload = workloadRepository.findByTrainerUsername(wr.trainerUsername()).
                orElseGet(()->{
                    Workload newWorkload = new Workload();
                    newWorkload.setTrainerUsername(wr.trainerUsername());
                    newWorkload.setTrainerFirstname(wr.trainerFirstname());
                    newWorkload.setTrainerLastname(wr.trainerLastname());
                    newWorkload.setIsActive(wr.isActive());
                   return newWorkload;
                });
        Integer year = wr.trainingDate().getYear();

        YearWorkload yearWorkload = workload.getYears().stream()
                .filter(x->x.getWorkloadYear().equals(year))
                .findAny()
                .orElseGet(()->{
                    YearWorkload newYear = new YearWorkload();
                    newYear.setWorkloadYear(year);
                    newYear.setMonths(new ArrayList<>());
                    workload.getYears().add(newYear);
                    return newYear;
                });
        Month month = wr.trainingDate().getMonth();

        MonthWorkload monthWorkload = yearWorkload.getMonths().stream()
                .filter(x->x.getMonth().equals(month))
                .findAny()
                .orElseGet(()->{
                    MonthWorkload newMonthWorkload = new MonthWorkload();
                    newMonthWorkload.setMonth(month);
                    yearWorkload.getMonths().add(newMonthWorkload);
                    return newMonthWorkload;
                });

        Long trainingDuration = wr.duration().longValue();
        Long currentWorkload = Optional.ofNullable(monthWorkload.getWorkload()).orElse(0L);
        monthWorkload.setWorkload(currentWorkload + trainingDuration);
        workloadRepository.save(workload);

        return true;
    }
}
