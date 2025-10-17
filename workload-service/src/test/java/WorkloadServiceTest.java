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
import com.epam.service.WorkloadServiceImpl;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkloadServiceTest {
    @Mock
    private YearRepository yearRepository;
    @Mock
    private MonthRepository monthRepository;
    @Mock
    private WorkloadRepository workloadRepository;
    @InjectMocks
    private WorkloadServiceImpl workloadService;

    TrainerWorkloadRequest add;
    TrainerWorkloadRequest delete;

    @BeforeEach
    public void initData(){
        add = new TrainerWorkloadRequest("trainer", "", "", true,
                LocalDateTime.now(), Duration.ofHours(1),
                TrainerWorkloadRequest.ActionType.ADD
        );
        delete = new TrainerWorkloadRequest("trainer",
                "",
                "",
                true, LocalDateTime.now(), Duration.ofHours(1), TrainerWorkloadRequest.ActionType.DELETE
        );
    }

    @Test
    void workloadEvent_whenAddAction_thenReturnAdd(){
        TrainerWorkload workload = new TrainerWorkload();
        workload.setTrainerUsername(add.trainerUsername());

        String result = workloadService.workloadEvent(add);

        assertEquals("ADD", result);
    }

    @Test
    void workloadEvent_whenDeleteAction_thenReturn(){
        TrainerWorkload workload = new TrainerWorkload();
        workload.setTrainerUsername(add.trainerUsername());

        when(workloadRepository.findByTrainerUsername("trainer"))
                .thenReturn(Optional.of(workload));

        String result = workloadService.workloadEvent(delete);

        assertEquals("DELETE", result);
    }

    @ParameterizedTest(name = "workloadResponse_source")
    @MethodSource("workloadResponse_source")
    void workloadResponse(List<Years> years){
        TrainerWorkload workload = new TrainerWorkload();
        workload.setYears(years);
        workload.setTrainerUsername("trainer");

        when(workloadRepository.findByTrainerUsername(workload.getTrainerUsername()))
                .thenReturn(Optional.of(workload));

        TrainerWorkloadResponse res = workloadService.workloadResponse(workload.getTrainerUsername());

        assertNotNull(res.years());
        assertFalse(res.years().isEmpty());
    }

    private static Stream<List<Years>> workloadResponse_source(){
        List<Months> months = Stream.of(0,1,2,3)
                .map(x->
                     new Months(LocalDateTime.now().plusMonths(x).getMonth(),x.longValue())
                ).toList();

        List<Years> years = Stream.of(0,1).map(x->
                new Years(x.longValue(),x.intValue(),months)
        ).toList();

        return Stream.of(years);
    }

    @Test
    void addWorkloadEvent_shouldSaveTrainerWorkload() {
        TrainerWorkloadRequest req = new TrainerWorkloadRequest(
                "trainer", "", "", true,
                LocalDateTime.now(), Duration.ofHours(1),
                TrainerWorkloadRequest.ActionType.ADD
        );

        TrainerWorkload mockTrainer = new TrainerWorkload();
        mockTrainer.setTrainerUsername("trainer");

        when(workloadRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        boolean result = workloadService.addWorkloadEvent(req);

        assertTrue(result);

        verify(workloadRepository).save(any());
    }

}
