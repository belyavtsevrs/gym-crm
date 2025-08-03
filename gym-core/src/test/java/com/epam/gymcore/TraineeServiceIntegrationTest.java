package com.epam.gymcore;

import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.service.TraineeService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TraineeServiceIntegrationTest {

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TraineeDao traineeDao;

    Trainee trainee;

    @BeforeEach
    void init(){
        trainee = new Trainee();
        trainee.setFirstName("Rodion");
        trainee.setLastName("B");
        trainee.setDateOfBirth(LocalDate.of(2000, 1, 1));
        trainee.setAddress("Almaty");
    }

    @Test
    void shouldSaveTraineeWithGeneratedUsernameAndPassword() {
        Trainee saved = traineeService.create(trainee);
        assertNotNull(saved.getId());
        assertNotNull(saved.getUsername());
        assertNotNull(saved.getPassword());

        Optional<Trainee> res = traineeDao.findById(saved.getId());
        assertTrue(res.isPresent());

        assertEquals("Rodion", res.get().getFirstName());
        assertEquals("B", res.get().getLastName());

        Trainee trainee2 = new Trainee();
        trainee2.setFirstName("Rodion");
        trainee2.setLastName("B");
        trainee2.setDateOfBirth(LocalDate.of(1999, 1, 1));
        trainee2.setAddress("Astana");

        Trainee saved2 = traineeService.create(trainee2);

        Optional<Trainee> res2 = traineeDao.findById(saved.getId());
        assertTrue(res2.isPresent());

        assertNotEquals(saved.getUsername(),saved2.getUsername());
    }

    @Test
    void shouldFindTraineeByUsernameAndPassword() {
        Trainee saved = traineeService.create(trainee);

        Optional<Trainee> res = traineeService.findByUsernameAndPassword(saved.getUsername(), saved.getPassword());

        assertTrue(res.isPresent());
        assertEquals("Rodion", res.get().getFirstName());
    }

    @Test
    void shouldUpdatePassword() {
        Trainee res = traineeService.create(trainee);

        String newPassword = "qjoewfeqwjjjwi";
        traineeService.updatePasswordByUsername(res.getUsername(), newPassword);

        Optional<Trainee> updated = traineeService.findByUsernameAndPassword(res.getUsername(), newPassword);
        assertTrue(updated.isPresent());
    }

    @Test
    void shouldDeleteById(){
        Trainee res = traineeService.create(trainee);

        traineeService.deleteById(res.getId());

        assertFalse(traineeService.findById(res.getId()).isPresent());
    }

    @Test
    void shouldFindAll(){
        traineeService.create(trainee);

        List<Trainee> res = traineeService.findAll();

        assertNotEquals(0,res.size());
    }

    @Test
    void shouldFindById(){
        Trainee saved = traineeService.create(trainee);

        Optional<Trainee> res = traineeService.findById(saved.getId());

        assertTrue(res.isPresent());
        assertEquals("Rodion", res.get().getFirstName());
    }

    @Test
    void shouldFindByUsername(){
        Trainee saved = traineeService.create(trainee);

        var res = traineeService.findByUsername(saved.getUsername());

        assertTrue(res.isPresent());
    }

    @Test
    void shouldUpdateTrainee() {
        Trainee saved = traineeService.create(trainee);

        saved.setAddress("New Address");
        traineeService.update(saved);

        var res = traineeService.findById(saved.getId());

        assertTrue(res.isPresent());
        assertTrue(res.get().getAddress().equalsIgnoreCase("New Address"));
    }

    @Test
    void shouldSetActiveStatus(){
        Trainee saved = traineeService.create(trainee);

        traineeService.setActiveStatusByUsername(saved.getUsername(), false);

        Optional<Trainee> updated = traineeService.findByUsername(saved.getUsername());

        assertTrue(updated.isPresent());
        assertFalse(updated.get().isActive());
    }

    @Test
    void shouldFindAllActive(){
        List<Trainee> result = traineeService.findAllActive();

        assertFalse(result.isEmpty());
    }

    @Test
    void shouldFindTrainingsByUsernameAndCriteria(){
        List<Training> res = traineeService.getUsersByUsernameAndCriteria("rodion.b", LocalDateTime.now(), LocalDateTime.now().plusMinutes(64));
        assertFalse(res.isEmpty());

    }
}
