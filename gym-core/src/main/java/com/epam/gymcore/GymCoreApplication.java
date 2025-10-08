package com.epam.gymcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients(basePackages = "com.epam.gymcore.client")
public class GymCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymCoreApplication.class, args);
    }

}
