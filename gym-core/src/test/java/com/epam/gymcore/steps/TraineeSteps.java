package com.epam.gymcore.steps;

import com.epam.gymcore.controller.TraineeController;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TraineeSteps {

    private MockMvc mockMvc;

    @Mock
    private TraineeService traineeService;

    private ResultActions resultActions;
    private String requestBody;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        TraineeController traineeController = new TraineeController(traineeService);
        mockMvc = MockMvcBuilders.standaloneSetup(traineeController).build();
    }

    // Registration steps
    @Given("I have trainee registration data with first name {string} and last name {string}")
    public void registrationTrainee(String firstName, String lastName) {
        requestBody = String.format("{\"firstName\":\"%s\",\"lastName\": \"%s\"}", firstName, lastName);
        when(traineeService.register(any()))
                .thenReturn(new UserDto(firstName, lastName));
    }

    @When("I register the trainee")
    public void registration() throws Exception {
        resultActions = mockMvc.perform(post("/api/trainee/register-trainee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    @Then("the registration should be successful with first name {string} and last name {string}")
    public void registration_success(String firstName, String lastName) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName));
    }

    // Update login steps
    @Given("there is a trainee with username {string} and password {string}")
    public void loginTrainee(String username, String password) {
        Trainee trainee = new Trainee("Rodion", "B", LocalDate.now(), "address");
        trainee.setUsername(username);
        when(traineeService.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.of(trainee));
    }

    @Given("there is no trainee with username {string} and password {string}")
    public void traineeNotFound(String username, String password) {
        when(traineeService.findByUsernameAndPassword(username, password))
                .thenReturn(Optional.empty());
    }

    @When("I update login for {string} with old password {string} and new password {string}")
    public void updateTraineeLogin(String username, String oldPassword, String newPassword) throws Exception {
        resultActions = mockMvc.perform(put("/api/trainee/{username}/update-login", username)
                .param("oldPassword", oldPassword)
                .param("newPassword", newPassword));
    }

    @Then("the password should be updated to {string} for username {string}")
    public void updatedTraineeLoginAndPassword(String newPassword, String username) {
        verify(traineeService).updatePasswordByUsername(username, newPassword);
    }

    @Then("the response should be unauthorized")
    public void login_successful() throws Exception {
        resultActions.andExpect(status().isUnauthorized());
    }

    // Profile steps
    @Given("there is a trainee with username {string}")
    public void getTraineeProfile(String username) {
        var trainee = new TraineeProfileDto(
                "rodion", "b", LocalDate.now(), "Address",
                true, new ArrayList<>()
        );

        when(traineeService.getTraineeProfile(username))
                .thenReturn(trainee);
    }

    @Given("there is no trainee with username {string}")
    public void traineeProfileIfNotExists(String username) {
        when(traineeService.getTraineeProfile(username))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));
    }

    @When("I get trainee profile for {string}")
    public void traineeProfileIsFound(String username) throws Exception {
        var trainee = new TraineeProfileDto("rodion", "b", LocalDate.now(), "Address", true, new ArrayList<>());
        when(traineeService.getTraineeProfile(username)).thenReturn(trainee);

        resultActions = mockMvc.perform(get("/api/trainee/{username}/get-profile", username));
    }

    @Then("the profile should contain first name {string}, last name {string}, and address {string}")
    public void traineeProfileFound(String firstName, String lastName, String address) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.address").value(address));
    }

    @Then("the response should be not found")
    public void traineeProfileNotFound() throws Exception {
        resultActions.andExpect(status().isNotFound());
    }

    // Update profile steps
    @When("I update trainee profile for {string} with data:")
    public void updateProfile(String username, String data) throws Exception {
        TraineeUpdateDto updateDto = new TraineeUpdateDto();
        updateDto.setFirstName("Rodion");
        updateDto.setLastName("B");
        updateDto.setAddress("Almaty");
        updateDto.setIsActive(true);

        TraineeProfileDto responseDto = new TraineeProfileDto(
                "Rodion", "B", LocalDate.now(), "Almaty", true, List.of()
        );

        when(traineeService.updateTraineeProfile(eq(username), any(TraineeUpdateDto.class)))
                .thenReturn(responseDto);

        resultActions = mockMvc.perform(put("/api/trainee/{username}/update-profile", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content(data));
    }

    @Then("the updated profile should contain first name {string}, last name {string}, address {string}, and active {string}")
    public void updatedProfile(String firstName, String lastName, String address, String active) throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.isActive").value(Boolean.parseBoolean(active)))
                .andExpect(jsonPath("$.trainersList").isArray());
    }

    // Available trainers steps
    @When("I get not assigned trainers for {string}")
    public void getAvailableTrainers(String username) throws Exception {
        when(traineeService.notAssignedTrainers(username))
                .thenReturn(List.of(new TrainerDto("t", "t", "t", new ArrayList<>())));

        resultActions = mockMvc.perform(get("/api/trainee/{username}/available-trainers", username)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the response should contain available trainers")
    public void listAvainableTtrainers() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specializations").isArray())
                .andExpect(jsonPath("$[0].username").value("t"))
                .andExpect(jsonPath("$[0].firstName").value("t"))
                .andExpect(jsonPath("$[0].lastName").value("t"));
    }

    // Update trainers steps
    @When("I update trainers list for {string} with trainers: {string}")
    public void updateTraineeList(String username, String trainers) throws Exception {
        List<String> trainerList = List.of("trainer1", "trainer2");
        List<TrainerDto> response = List.of(new TrainerDto("t", "t", "t", new ArrayList<>()));

        when(traineeService.updateTrainerList(username, trainerList)).thenReturn(response);

        resultActions = mockMvc.perform(put("/api/trainee/{username}/update-trainee-trainers", username)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[\"trainer1\",\"trainer2\"]"));
    }

    @Then("the response should contain updated trainers list")
    public void updatedTraineeList_successful() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("t"));
    }

    // Training list steps
    @When("I get training list for {string} with period from {string} to {string} and trainee name {string}")
    public void fetchingTrainingList(String username, String from, String to, String traineeName) throws Exception {
        List<TrainingDto> trainings = List.of(
                new TrainingDto("vasily.che", LocalDateTime.now(), "Bodybuilding", 2L, "rodion.b")
        );

        when(traineeService.traineeTrainingsList(eq(username), any(), any(), any()))
                .thenReturn(trainings);

        resultActions = mockMvc.perform(get("/api/trainee/{username}/training-list", username)
                .param("from", from)
                .param("to", to)
                .param("traineeName", traineeName)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the response should contain training list")
    public void listTrainingTypes() throws Exception {
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingType").value("Bodybuilding"));
    }

    // Delete steps
    @When("I delete trainee with username {string}")
    public void deleteTrainee(String username) throws Exception {
        doNothing().when(traineeService).deleteByUsername(username);

        resultActions = mockMvc.perform(delete("/api/trainee/delete")
                .param("username", username));
    }

    @Then("the trainee should be deleted")
    public void traineeDeleted() throws Exception {
        resultActions.andExpect(status().isOk());
        verify(traineeService).deleteByUsername(anyString());
    }

    // Status change steps
    @When("I change status for {string} to {string}")
    public void changeTraineeStatus(String username, String status) throws Exception {
        when(traineeService.changeStatus(anyString(), anyBoolean()))
                .thenReturn(true);

        resultActions = mockMvc.perform(patch("/api/trainee/change-status")
                .param("username", username)
                .param("isActive", status)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Then("the status should be updated successfully")
    public void TraineeStatusChanged() throws Exception {
        resultActions.andExpect(status().isOk());
    }
}