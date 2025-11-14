package com.epam.gymcore.steps;


import com.epam.gymcore.service.TrainerService;
import io.cucumber.java.en.When;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

public class TrainerSteps {
    private MockMvc mockMvc;
    @Mock
    private TrainerService trainerService;

    private ResultActions resultAction;
    private String requestBody;

    @When("I have a valid trainer request")
    void givenTrainer(){
    }
}
