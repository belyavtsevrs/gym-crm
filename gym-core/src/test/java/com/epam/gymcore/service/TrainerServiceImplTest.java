package com.epam.gymcore.service;

import com.epam.gymcore.dao.TrainerDaoImpl;
import com.epam.gymcore.domain.entity.TraineeImpl;
import com.epam.gymcore.domain.entity.TrainerImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.domain.model.Trainer;
import com.epam.gymcore.service.Impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceImplTest {

    @Mock
    private TrainerDaoImpl trainerDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;

    @BeforeEach
    public void init() {
        trainer = new TrainerImpl(
                1L,
                "Vasily",
                "Che",
                "Vasily.Che",
                "123321421",
                true,
                "Fitness"
        );
    }

    @Test
    void testCreateTrainer() {
        Trainer result = trainerService.create(trainer);

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerDao).save(captor.capture());

        Trainer saved = captor.getValue();
        assertEquals("Vasily.Che", saved.getUsername());
        assertEquals("Fitness", saved.getSpecialization());
        assertTrue(saved.isActive());
    }

    @Test
    void testFindAllTrainers(){
        List<Trainer> trainers = List.of(trainer);

        when(trainerDao.findAll()).thenReturn(trainers);

        List<Trainer> res = trainerService.findAll();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("Vasily", res.get(0).getFirstName());

        verify(trainerDao).findAll();
    }

    @Test
    public void testFindById(){
        when(trainerDao.findById(trainer.getId())).thenReturn(Optional.of(trainer));

        Optional<Trainer> result = trainerDao.findById(trainer.getId());

        assertNotEquals(null,result.get());
        assertEquals("Vasily.Che",result.get().getUsername());
    }

    @Test
    public void testUpdateTrainee() {
        Long id = trainer.getId();
        when(trainerDao.findById(id)).thenReturn(Optional.of(trainer));

        Trainer updated = new TrainerImpl(
                id,
                "Vasily",
                "C",
                "Vasily.C",
                "123321421",
                true,
                "Fitness"
        );


        Trainer updatedTrainee = trainerService.update(updated);

        assertEquals("Vasily", updatedTrainee.getFirstName());
        assertEquals("C", updatedTrainee.getLastName());
        assertEquals("Vasily.C",updatedTrainee.getUsername());

        verify(trainerDao).save(any(Trainer.class));
    }
}
