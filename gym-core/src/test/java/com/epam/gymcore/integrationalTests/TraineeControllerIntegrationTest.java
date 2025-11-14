package com.epam.gymcore.integrationalTests;

import com.epam.gymcore.domain.dto.RegisterRequest;
import com.epam.gymcore.domain.dto.TrainerDto;
import com.epam.gymcore.domain.dto.TrainingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TraineeControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private RegisterRequest registerRequest;
    private String token;

    @JmsListener(destination = "registration.queue")
    public void intercept(RegisterRequest request) {
        log.info("the message is intercepted: {}", request);
        registerRequest = request;
    }

    @Test
    @When("The trainer registers")
    @Order(1)
    void registration_successful() {
        String body = """
            {
                "firstName": "trainee",
                "lastName": "test",
                "dateOfBirth": "2001-01-01T01:01:01",
                "address": "test address"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        log.info("Sending request: {}", request);
        restTemplate.postForEntity("/api/trainee/register-trainee", request, String.class);

        Awaitility.await().atMost(Duration.ofSeconds(5)).until(() ->
                registerRequest != null && registerRequest.username() != null
        );

        log.info("Intercepted request: {}", registerRequest);

        assertEquals("trainee.test", registerRequest.username());
        assertNotNull(registerRequest.password());
    }
    @Test
    @Order(2)
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
    @Order(3)
    void getTraineeProfile() {
        HttpHeaders headers = new HttpHeaders();
        log.info("token = {}",token);
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/trainee/rodion.b/get-profile",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"firstName\":\"Rodion\""));
        assertTrue(body.contains("\"dateOfBirth\""));
    }

    @Test
    @Order(4)
    void updTraineeProfile() {
        String bodyReq = """
        {
            "firstName": "test123"
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        log.info("token = {}", token);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(bodyReq, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainee/rodion.b/update-profile",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"firstName\":\"test123\""));
    }

    @Test
    void notAssignedTrainers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List<TrainerDto>> response = restTemplate.exchange(
                "/api/trainee/rodion.b/available-trainers",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        log.info("trainers list = {}",response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void  updateTraineeTrainers(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
                [
                    "batyrbek.batyrbekovich"
                ]
                """;

        HttpEntity<String> request = new HttpEntity<>(body,headers);

        ResponseEntity<List<TrainerDto>> response = restTemplate.exchange(
                "/api/trainee/rodion.b/update-trainee-trainers",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<>() {}
        );

        log.info("trainers list = {}",response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getTodaysTrainingList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

        String url = String.format("/api/trainee/rodion.b/training-list?from=%s&to=%s",
                startOfDay, endOfDay);

        ResponseEntity<TrainingDto[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                TrainingDto[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TrainingDto[] trainings = response.getBody();
        assertNotNull(trainings);
        assertTrue(trainings.length > 0);
        log.info("Trainings today: {}", Arrays.toString(trainings));
    }

    @Test
    void changeTrainerStatus() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainee/change-status?username=rodion.b&isActive=false",
                HttpMethod.PATCH,
                request,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<String> profile = restTemplate.exchange(
                "/api/trainee/rodion.b/get-profile",
                HttpMethod.GET,
                request,
                String.class
        );

        String body = profile.getBody();
        JsonNode json = new ObjectMapper().readTree(body);
        boolean isActive = json.get("isActive").asBoolean();

        assertFalse(isActive);
    }
}
