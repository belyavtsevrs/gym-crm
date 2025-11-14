package com.epam.gymcore.componentTests;

import com.epam.gymcore.controller.TrainingController;
import com.epam.gymcore.domain.dto.CreateTrainingDto;
import com.epam.gymcore.domain.dto.TrainingDto;
import com.epam.gymcore.service.TrainingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrainingController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrainingControllerComponentTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainingService trainingService;

    @Test
    void addTraining_success() throws Exception {
        when(trainingService.createTraining(any(CreateTrainingDto.class)))
                .thenReturn(new TrainingDto("vasily.che",LocalDateTime.now(),"Bodybuilding",2L,"rodion.b"));

        mockMvc.perform(post("/api/trainings/create-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"traineeUsername\":\"vasily.che\",\"trainerUsername\":\"rodion.b\",\"trainingName\":\"Bodybuilding\"}")
                .with(csrf())
        ).andExpect(status().isOk());
    }

    @Test
    void addTraining_fail() throws Exception {
        when(trainingService.createTraining(new CreateTrainingDto("vasily.che",LocalDateTime.now().minusMonths(1),"Bodybuilding",Duration.ofHours(2),"rodion.b")))
                .thenReturn(any());

        mockMvc.perform(post("/api/trainings/create-training")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"traineeUsername\":\"vasily.che\",\"trainerUsername\":\"rodion.b\",\"trainingName\":\"Bodybuilding\"}")
                .with(csrf())
        ).andExpect(status().isBadRequest());
    }


}
