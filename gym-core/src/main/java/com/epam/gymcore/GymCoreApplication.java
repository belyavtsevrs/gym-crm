package com.epam.gymcore;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GymCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCoreApplication.class, args);
    }

}
