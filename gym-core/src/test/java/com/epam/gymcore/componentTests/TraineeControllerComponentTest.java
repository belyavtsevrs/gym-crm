package com.epam.gymcore.componentTests;

import com.epam.gymcore.controller.TraineeController;
import com.epam.gymcore.domain.dto.*;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TraineeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TraineeControllerComponentTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TraineeService traineeService;

    @Test
    void traineeRegistration() throws Exception {
        when(traineeService.register(any()))
                .thenReturn(new UserDto("name","surname"));

        mockMvc.perform(post("/api/trainee/register-trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"name\",\"lastName\": \"surname\"}")
                )
                .andExpect(jsonPath("$.firstName").value("name"))
                .andExpect(jsonPath("$.lastName").value("surname"))
                .andExpect(status().isOk());
    }

    @Test
    void updateLogin_success() throws Exception {
        Trainee trainee = new Trainee("Rodion",
                "B",
                LocalDate.now(),
                "address");
        trainee.setUsername("rodion.b");

        when(traineeService.findByUsernameAndPassword("rodion.b", "old123"))
                .thenReturn(Optional.of(trainee));

        mockMvc.perform(put("/api/trainee/rodion.b/update-login")
                        .param("oldPassword", "old123")
                        .param("newPassword", "qwerty")
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(traineeService).updatePasswordByUsername("rodion.b", "qwerty");
    }

    @Test
    void updateLogin_fail() throws Exception {
        when(traineeService.findByUsernameAndPassword(eq("rodion"), eq("wrongOldPass")))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/trainee/rodion/update-login")
                        .param("oldPassword", "wrongOldPass")
                        .param("newPassword", "newSecurePass")
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getTraineeProfile_success() throws Exception {
        var trainee = new TraineeProfileDto("rodion",
                "b",LocalDate.now(),"Address",true, new ArrayList<>());
        when(traineeService.getTraineeProfile("rodion.b"))
                .thenReturn(trainee);

        mockMvc.perform(get("/api/trainee/rodion.b/get-profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("rodion"))
                .andExpect(jsonPath("$.lastName").value("b"))
                .andExpect(jsonPath("$.address").value("Address"));
    }

    @Test
    void getTraineeProfile_notFound() throws Exception {
        when(traineeService.getTraineeProfile("rodion.b"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Trainee not found"));

        mockMvc.perform(get("/api/trainee/rodion.b/get-profile"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTraineeProfile_ShouldReturnUpdatedProfile() throws Exception {
        TraineeUpdateDto updateDto = new TraineeUpdateDto();
        updateDto.setFirstName("Rodion");
        updateDto.setLastName("B");
        updateDto.setAddress("Almaty");
        updateDto.setIsActive(true);

        TraineeProfileDto responseDto = new TraineeProfileDto(
                "Rodion",
                "B",
                LocalDate.of(2000, 1, 1),
                "Almaty",
                true,
                List.of()
        );

        when(traineeService.updateTraineeProfile(eq("rodion.b"), any(TraineeUpdateDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/trainee/rodion.b/update-profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Rodion",
                                    "lastName": "B",
                                    "address": "Almaty",
                                    "isActive": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Rodion"))
                .andExpect(jsonPath("$.lastName").value("B"))
                .andExpect(jsonPath("$.address").value("Almaty"))
                .andExpect(jsonPath("$.isActive").value(true))
                .andExpect(jsonPath("$.trainersList").isArray());
    }

    @Test
    void notAssignedTrainers() throws Exception {
        when(traineeService.notAssignedTrainers("rodion.b"))
                .thenReturn(List.of(new TrainerDto("t","t","t",new ArrayList<>())));

        mockMvc.perform(get("/api/trainee/rodion.b/available-trainers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specializations").isArray())
                .andExpect(jsonPath("$[0].username").value("t"))
                .andExpect(jsonPath("$[0].firstName").value("t"))
                .andExpect(jsonPath("$[0].lastName").value("t"));
    }

    @Test
    void updateTraineeTrainersTest() throws Exception {
        List<String> trainers = List.of("trainer1", "trainer2");
        List<TrainerDto> response = List.of(new TrainerDto("t", "t", "t", new ArrayList<>()));

        when(traineeService.updateTrainerList("rodion.b", trainers)).thenReturn(response);

        mockMvc.perform(put("/api/trainee/rodion.b/update-trainee-trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"trainer1\",\"trainer2\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("t"));
    }

    @Test
    void getTraineeTrainingListTest() throws Exception {
        List<TrainingDto> trainings = List.of(
                new TrainingDto("vasily.che", LocalDateTime.now(), "Bodybuilding", 2L, "rodion.b")
        );

        when(traineeService.traineeTrainingsList(eq("rodion.b"), any(), any(), any()))
                .thenReturn(trainings);

        mockMvc.perform(get("/api/trainee/rodion.b/training-list")
                        .param("from", "2025-01-01T00:00:00")
                        .param("to", "2025-12-31T23:59:59")
                        .param("traineeName", "Rodion")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingType").value("Bodybuilding"));
    }

    @Test
    void deleteTraineeTest() throws Exception {
        doNothing().when(traineeService).deleteByUsername("rodion.b");

        mockMvc.perform(delete("/api/trainee/delete")
                        .param("username", "rodion.b"))
                .andExpect(status().isOk());
    }

    @Test
    void changeStatusTest() throws Exception {
        when(traineeService.changeStatus(anyString(), anyBoolean()))
                .thenReturn(true);

        mockMvc.perform(patch("/api/trainee/change-status")
                        .param("username", "rodion.b")
                        .param("isActive", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
