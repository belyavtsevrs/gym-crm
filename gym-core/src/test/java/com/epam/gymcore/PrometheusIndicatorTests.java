package com.epam.gymcore;

import com.epam.gymcore.actuator.CustomMetric;
import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.dao.TrainerDao;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@ExtendWith(MockitoExtension.class)
class PrometheusIndicatorTest {
    @Mock
    private TraineeDao traineeDao;
    @Mock
    private TrainerDao trainerDao;
    @InjectMocks
    private CustomMetric metric;

    @Test
    void indicatorsTest() {
        MeterRegistry registry = new SimpleMeterRegistry();
        metric.bindTo(registry);

        when(traineeDao.getCount()).thenReturn(5L);
        when(trainerDao.getCount()).thenReturn(2L);

        assertThat(registry.find("trainee_amount").gauge().value()).isEqualTo(5.0);
        assertThat(registry.find("trainer_amount").gauge().value()).isEqualTo(2.0);
    }
}
