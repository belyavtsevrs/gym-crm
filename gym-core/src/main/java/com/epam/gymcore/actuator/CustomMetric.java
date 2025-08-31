package com.epam.gymcore.actuator;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.dao.TrainerDao;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

@Component
public class CustomMetric implements MeterBinder {
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    public CustomMetric(TraineeDao traineeDao, TrainerDao trainerDao) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Gauge.builder("trainee_amount",traineeDao, AbstractUserDao::getCount)
                .description("total trianees").register(meterRegistry);

        Gauge.builder("trainer_amount",trainerDao,AbstractUserDao::getCount)
                .description("total graining").register(meterRegistry);
    }
}
