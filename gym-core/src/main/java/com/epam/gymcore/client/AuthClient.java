package com.epam.gymcore.client;

import com.epam.gymcore.domain.dto.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name="gym-security")
public interface AuthClient {

    @PostMapping("/auth/register")
    Map<String,Object> createUser(RegisterRequest request);

}