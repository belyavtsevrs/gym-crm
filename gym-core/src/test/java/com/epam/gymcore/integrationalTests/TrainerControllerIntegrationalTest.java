package com.epam.gymcore.integrationalTests;

import com.epam.gymcore.domain.dto.RegisterRequest;
import com.epam.gymcore.domain.dto.TrainerWorkloadResponse;
import com.epam.gymcore.domain.dto.TrainingDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TrainerControllerIntegrationalTest {
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
                "firstName": "trainer",
                "lastName": "test"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        log.info("Sending request: {}", request);
        restTemplate.postForEntity("/api/trainer/register-trainer", request, String.class);

        Awaitility.await().atMost(Duration.ofSeconds(5)).until(() ->
                registerRequest != null && registerRequest.username() != null
        );

        log.info("Intercepted request: {}", registerRequest);

        assertEquals("trainer.test", registerRequest.username());
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
    void getTrainerProfile() {
        HttpHeaders headers = new HttpHeaders();
        log.info("token = {}",token);
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/trainer/vasily.che/get-profile",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"firstName\":\"Vasily\""));
        assertTrue(body.contains("\"trainees\""));
        assertTrue(body.contains("\"specializations\""));
    }

    @Test
    @Order(4)
    void updTrainerProfile() {
        String bodyReq = """
        {
            "firstName": "Vasily111"
        }
        """;

        HttpHeaders headers = new HttpHeaders();
        log.info("token = {}", token);
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(bodyReq, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:8080/api/trainer/vasily.che/update-profile",
                HttpMethod.PUT,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"firstName\":\"Vasily111\""));
    }

    @Test
    void getTrainerTrainingListWithinWeek() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        LocalDateTime from = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime to = from.plusDays(7).withHour(23).withMinute(59).withSecond(59);

        String fromStr = from.format(DateTimeFormatter.ISO_DATE_TIME);
        String toStr = to.format(DateTimeFormatter.ISO_DATE_TIME);

        String url = String.format("/api/trainer/vasily.che/training-list?from=%s&to=%s", fromStr, toStr);

        ResponseEntity<List<TrainingDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<TrainingDto> trainings = response.getBody();
        assertNotNull(trainings);
        assertFalse(trainings.isEmpty());


        log.info("trainings in the next week = {}", trainings);
    }

    @Test
    void getTodaysTrainingList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999_999_999);

        String url = String.format("/api/trainer/vasily.che/training-list?from=%s&to=%s",
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
    void getTrainerWorkload() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainer/vasily.che/trainer-workload",
                HttpMethod.GET,
                request,
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        String body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"username\":\"vasily.che\""));
    }

    @Test
    void changeTrainerStatus() throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/trainer/change-status?username=vasily.che&isActive=false",
                HttpMethod.PATCH,
                request,
                String.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<String> profile = restTemplate.exchange(
                "/api/trainer/vasily.che/get-profile",
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

