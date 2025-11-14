package com.epam.gymcore.steps;

import com.epam.gymcore.controller.TrainingController;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.TrainingService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrainingSteps {

    private MockMvc mockMvc;

    @Mock
    private TrainingService trainingService;

    private String requestBody;
    private ResultActions resultActions;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        TrainingController trainingController = new TrainingController(trainingService);
        mockMvc = MockMvcBuilders.standaloneSetup(trainingController).build();
    }

    @Given("I have a valid training request")
    public void registration_success() {
        requestBody = "{\"traineeUsername\":\"vasily.che\",\"trainerUsername\":\"rodion.b\",\"trainingName\":\"Bodybuilding\"}";

        when(trainingService.createTraining(any(CreateTrainingDto.class)))
                .thenReturn(new TrainingDto("vasily.che", LocalDateTime.now(), "Bodybuilding", 2L, "rodion.b"));
    }

    @Given("I have an invalid training request")
    public void registration_fail() {
        requestBody = "{\"traineeUsername\":\"vasily.che\",\"trainerUsername\":\"rodion.b\",\"trainingName\":\"Bodybuilding\"}";

        when(trainingService.createTraining(any(CreateTrainingDto.class)))
                .thenReturn(null);
    }

    @When("I send a POST request to {string}")
    public void registration(String url) throws Exception {
        resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    @Then("the response status should be {int}")
    public void result(Integer expectedStatus) throws Exception {
        resultActions.andExpect(status().is(expectedStatus));
    }
}