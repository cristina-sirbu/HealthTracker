package com.healthtracker.healthtracker;

import com.healthtracker.healthtracker.export.ExportService;
import com.healthtracker.healthtracker.medication.api.MedicationController;
import com.healthtracker.healthtracker.medication.app.MedicationService;
import com.healthtracker.healthtracker.medication.domain.Medication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(MedicationController.class)
class MedicationControllerWebTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private MedicationService medicationService;

    @MockitoBean
    private ExportService exportService;


    @WithMockUser(username = "alice", roles = "USER")
    @Test
    void createMedication_ValidRequest_returnOk() throws Exception {
        when(medicationService.create(anyLong(), anyString(), anyString(), anyString()))
                .thenReturn(new Medication(1L, 1L, "Ibuprofen", "tablet", "200mg",
                        OffsetDateTime.now()));
        mockMvc.perform(post("/users/1/medications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Ibuprofen\",\"form\":\"tablet\",\"strength\":\"200mg\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofen"));
    }

}
