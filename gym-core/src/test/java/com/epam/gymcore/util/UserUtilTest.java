package com.epam.gymcore.util;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

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

    }
}
