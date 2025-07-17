package com.epam.gymcore.service;

import com.epam.gymcore.dao.TraineeDaoImpl;
import com.epam.gymcore.domain.entity.TraineeImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.service.Impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceImplTest {
    @Mock
    private TraineeDaoImpl traineeDao;
    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee newTrainee;

    @BeforeEach
    public void init(){
        newTrainee = new TraineeImpl(
                1L,
                "Rodion",
                "B",
                "Rodion.B",
                "password123",
                true,
                LocalDate.now(),
                "Almaty"
        );
    }
    @Test
    void testCreateTrainee() {
        Mockito.when(traineeDao.findAll()).thenReturn(List.of());
        Trainee result = traineeService.create(newTrainee);

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeDao).save(captor.capture());

        Trainee saved = captor.getValue();
        assertEquals("Rodion.B",saved.getUsername());
    }

    @Test
    void testSuffixInNickname(){
        List<Trainee> existData = List.of(new TraineeImpl(
                "Rodion", "B", "Rodion.B", "epeworiqwr", true,
                LocalDate.of(1990, 10, 11), "CITY"
        ));

        Mockito.when(traineeDao.findAll()).thenReturn(existData);

        Trainee result = traineeService.create(newTrainee);

        assertEquals("Rodion.B#2", result.getUsername());
    }

    @Test
    public void testUpdateTrainee() {
        Long id = newTrainee.getId();
        when(traineeDao.findById(id)).thenReturn(Optional.of(newTrainee));

        Trainee updated = new TraineeImpl(
                "Rodion", "Bel", null, "password123", true,
                LocalDate.of(1999, 1, 1), "Almaty"
        );
        updated.setId(newTrainee.getId());

        Trainee updatedTrainee = traineeService.update(updated);

        assertEquals("Rodion", updatedTrainee.getFirstName());
        assertEquals("Bel", updatedTrainee.getLastName());
        assertEquals("Rodion.Bel",updatedTrainee.getUsername());
    }

}