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

    @Test
    void createAndTestMedicationPagination() throws Exception {
        // Register User
        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"password\":\"pass\"}"))
                .andExpect(status().isOk());

        // Add medication
        mockMvc.perform(post("/users/1/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"MedA\",\"form\":\"tablet\",\"strength\":\"10mg\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/1/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"MedB\",\"form\":\"tablet\",\"strength\":\"20mg\"}"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/users/1/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"MedC\",\"form\":\"tablet\",\"strength\":\"30mg\"}"))
                .andExpect(status().isOk());

        // List medications
        mockMvc.perform(get("/users/1/medications")
                        .param("limit","2")
                        .param("offset","2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3))
                .andExpect(jsonPath("$.limit").value(2))
                .andExpect(jsonPath("$.offset").value(2))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].name").value("MedC"));
    }

    @Test
    void testInvalidMedicationPagination() throws Exception {
        mockMvc.perform(get("/users/1/medications")
                        .param("limit","-1")
                        .param("offset","0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/users/1/medications")
                        .param("limit","1000")
                        .param("offset","0"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/users/1/medications")
                        .param("limit","1")
                        .param("offset","-1"))
                .andExpect(status().isBadRequest());
    }
}
