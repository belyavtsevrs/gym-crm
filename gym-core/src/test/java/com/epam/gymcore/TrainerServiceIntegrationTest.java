package com.epam.gymcore;

import com.epam.gymcore.dao.TrainerDao;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TrainerServiceIntegrationTest {
    @Autowired
    private TrainerService trainerService;

    @Autowired
    private TrainerDao trainerDao;

    Trainer trainer;

    @BeforeEach
    void init() {
        trainer = new Trainer("Vasily","Che"
        );
    }

    @Test
    void shouldSaveTrainerWithGeneratedUsernameAndPassword() {
        Trainer saved = trainerService.create(trainer);
        assertNotNull(saved.getId());
        assertNotNull(saved.getUsername());
        assertNotNull(saved.getPassword());

        Optional<Trainer> res = trainerDao.findById(saved.getId());
        assertTrue(res.isPresent());
        assertEquals("Vasily", res.get().getFirstName());

        Trainer trainer2 = new Trainer("Vasily","Che" );

        Trainer saved2 = trainerService.create(trainer2);

        assertNotEquals(saved.getUsername(), saved2.getUsername());
    }

    @Test
    void shouldFindTraineeByUsernameAndPassword() {
        Trainer saved = trainerService.create(trainer);

        Optional<Trainer> res = trainerService.findByUsernameAndPassword(saved.getUsername(), saved.getPassword());

        assertTrue(res.isPresent());
        assertEquals("Vasily", res.get().getFirstName());
    }

    @Test
    void shouldUpdatePassword() {
        Trainer res = trainerService.create(trainer);

        String newPassword = "qjoewfeqwjjjwi";
        trainerService.updatePasswordByUsername(res.getUsername(), newPassword);

        Optional<Trainer> updated = trainerService.findByUsernameAndPassword(res.getUsername(), newPassword);
        assertTrue(updated.isPresent());
    }


    @Test
    void shouldFindAll(){
        trainerService.create(trainer);

        List<Trainer> res = trainerService.findAll();

        assertNotEquals(0,res.size());
    }

    @Test
    void shouldFindById(){
        Trainer saved = trainerService.create(trainer);

        Optional<Trainer> res = trainerService.findById(saved.getId());

        assertTrue(res.isPresent());
        Trainer trainer = res.get();

        assertEquals("Vasily", trainer.getFirstName());
    }

    @Test
    void shouldFindByUsername(){
        Trainer saved = trainerService.create(trainer);

        var res = trainerService.findByUsername(saved.getUsername());

        assertTrue(res.isPresent());
    }

    @Test
    void shouldUpdateTrainee() {
        Trainer saved = trainerService.create(trainer);

        trainerService.update(saved);

        var res = trainerService.findById(saved.getId());

        assertTrue(res.isPresent());
    }

    @Test
    void shouldSetActiveStatus(){
        Trainer saved = trainerService.create(trainer);

        trainerService.setActiveStatusByUsername(saved.getUsername(), false);

        Optional<Trainer> updated = trainerService.findByUsername(saved.getUsername());

        assertTrue(updated.isPresent());
        assertFalse(updated.get().getIsActive());
    }

    @Test
    void shouldFindAllActive(){
        List<Trainer> result = trainerService.findAllActive();

        assertFalse(result.isEmpty());
    }

}
