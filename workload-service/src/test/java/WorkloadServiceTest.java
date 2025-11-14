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
import java.util.*;
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
                LocalDateTime.now(), 1,
                TrainerWorkloadRequest.ActionType.ADD
        );
        delete = new TrainerWorkloadRequest("trainer",
                "",
                "",
                true, LocalDateTime.now(), 1, TrainerWorkloadRequest.ActionType.DELETE
        );
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
                LocalDateTime.now(), 1,
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

    @Test
    void removeWorkloadEvent_shouldRemoveTrainerWorkload_returnTrue(){
        TrainerWorkload tw = new TrainerWorkload();
        tw.setTrainerUsername(delete.trainerUsername());
        tw.setYears(List.of(new Years(1l,2025,List.of(new Months(Month.OCTOBER,3l)))));

        Years years = tw.getYears().get(0);
        Month month = years.getMonths().get(0).getMonth();
        int year = tw.getYears().get(0).getWorkloadYear();
        String username = tw.getTrainerUsername();

        when(workloadRepository.findByTrainerUsername(username)).thenReturn(Optional.of(tw));
        when(yearRepository.findYearByWorkloadYearAndTrainerWorkload(year,tw)).thenReturn(years);
        when(monthRepository.findMonthsByMonthAndYearsRef(month,years)).thenReturn(years.getMonths().get(0));

        boolean res = workloadService.removeWorkloadEvent(delete);

        assertTrue(res);
        verify(monthRepository).save(any());
    }

    @Test
    void removeWorkloadEvent_shouldRemoveMonth_returnTrue(){
        TrainerWorkload tw = new TrainerWorkload();
        tw.setTrainerUsername(delete.trainerUsername());

        Months october = new Months(Month.OCTOBER, 1L);
        Years years = new Years(1L, 2025, new ArrayList<>(List.of(october)));
        tw.setYears(new ArrayList<>(List.of(years)));

        int year = years.getWorkloadYear();
        String username = tw.getTrainerUsername();

        when(workloadRepository.findByTrainerUsername(username)).thenReturn(Optional.of(tw));
        when(yearRepository.findYearByWorkloadYearAndTrainerWorkload(year, tw)).thenReturn(years);
        when(monthRepository.findMonthsByMonthAndYearsRef(Month.OCTOBER, years)).thenReturn(october);

        boolean res = workloadService.removeWorkloadEvent(delete);

        assertTrue(res);
        verify(monthRepository).delete(any());
    }

    @Test
    void removeWorkloadEvent_emptyYears_returnFalse(){
        TrainerWorkload tw = new TrainerWorkload();
        tw.setTrainerUsername(delete.trainerUsername());
        tw.setYears(List.of(new Years(1l,2025,List.of(new Months(Month.OCTOBER,1l)))));

        Years years = tw.getYears().get(0);
        int year = tw.getYears().get(0).getWorkloadYear();
        String username = tw.getTrainerUsername();

        when(workloadRepository.findByTrainerUsername(username)).thenReturn(Optional.of(tw));
        when(yearRepository.findYearByWorkloadYearAndTrainerWorkload(year,tw)).thenReturn(years);

        boolean res = workloadService.removeWorkloadEvent(delete);

        assertFalse(res);
    }
}
