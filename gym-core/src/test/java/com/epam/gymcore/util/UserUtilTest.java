package com.epam.gymcore.util;


import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.domain.entity.Trainee;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserUtilTest {
    @Autowired
    private TraineeDao traineeDao;

    @Test
    void generatePasswordTest(){
        String password = UserUtil.generatePassword();

        log.info(password);
        assertNotNull(password);
        assertEquals(10,password.length());
    }
    @Test
    void createUsernameTest(){
        Trainee trainee = new Trainee("Rodion","B", LocalDate.now(),"address");
        String generatedUsername = UserUtil.createUsername(trainee, username ->
                traineeDao.findByUsername(username).isPresent()
        );
        log.info("trainee = {}",generatedUsername);
        assertNotEquals("rodion.b",generatedUsername);
    }
}
