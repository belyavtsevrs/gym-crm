package com.epam.gymcore.integrationalTests;

import com.epam.gymcore.domain.dto.RegisterRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrainingControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @Test
    @Order(1)
    void login_successful() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.setBasicAuth("test.user", "123");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("scope", "read");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:8081/oauth2/token",
                request,
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        token = (String) response.getBody().get("access_token");
        assertNotNull(token);

        log.info("access token = {}",token);
    }

    @Test
    @Order(2)
    void createTraining() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        String body = """
                {
                    "trainerName" : "vasily.che",
                    "trainingDate" : "2026-01-01T01:01:01",
                    "trainingType" : "Bodybuilding",
                    "duration" : "PT2H",
                    "traineeName" : "rodion.b"
                }
                """;

        HttpEntity<String> request = new HttpEntity<>(body,headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainings/create-training",
                HttpMethod.POST,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(3)
    void removeTraining() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainings/vasily.che/delete?date=2026-01-01T01:01:01",
                HttpMethod.DELETE,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getTrainingTypes() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainings/types-of-training",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        assertNotNull(body);
        JsonNode json = new ObjectMapper().readTree(body);
        assertTrue(json.isArray());
        assertTrue(json.size() > 0);
    }
}
