package com.epam.gymsecurity;

import com.epam.gymsecurity.domain.dto.RegisterRequest;
import com.epam.gymsecurity.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymSecurityApplication implements ApplicationRunner {
	private final UserService userService;

    public GymSecurityApplication(UserService userService) {
        this.userService = userService;
    }

    public static void main(String[] args) {
		SpringApplication.run(GymSecurityApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		userService.register(new RegisterRequest("test.user","123","test"));
	}
}
