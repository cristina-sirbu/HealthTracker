package com.healthtracker.healthtracker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = HealthtrackerApplication.class)
@AutoConfigureMockMvc
class MedicationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createAndListMedication() throws Exception {
        // Register User
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"password\":\"pass\"}"))
                .andExpect(status().isOk());

        // Add medication
        mockMvc.perform(post("/users/1/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ibuprofen\",\"form\":\"tablet\",\"strength\":\"200mg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofen"));

        // List medications
        mockMvc.perform(get("/users/1/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ibuprofen"));

        // Duplicating the medication name for the same user should return 400
        mockMvc.perform(post("/users/1/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ibuprofen\"}"))
                .andExpect(status().isBadRequest());
    }
}
