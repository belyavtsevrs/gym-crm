package com.epam.gymcore.util;


import com.epam.gymcore.domain.entity.TraineeImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class UserUtilTest {
    @Test
    void generatePasswordTest(){
        String password = UserUtil.generatePassword();

        log.info(password);
        assertNotNull(password);
        assertEquals(10,password.length());
    }
    @Test
    void createUsernameTest(){
        Trainee newTrainee = new TraineeImpl(1L, "Rodion", "Bel", null, "password123", true, LocalDate.now(), "Almaty");

        List<User> users = List.of(
                new TraineeImpl(2L, "Rodion", "Bel", "Rodion.Bel", "password123", true, LocalDate.now(), "Almaty")
        );
        String userName = UserUtil.createUsername(newTrainee, users);
        log.info(userName);
        assertEquals("Rodion.Bel#2", userName,userName);
    }
}
