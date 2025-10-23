package com.healthtracker.healthtracker;

import com.healthtracker.healthtracker.medication.app.MedicationService;
import com.healthtracker.healthtracker.medication.domain.Medication;
import com.healthtracker.healthtracker.medication.infra.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicationServiceTest {
    @Mock
    MedicationRepository repo;
    @InjectMocks
    MedicationService service;

    @Test
    void addMedication() {
        Medication m = new Medication();
        m.setUserId(1L);
        m.setName("Test medication");
        m.setForm("Liquid");
        m.setStrength("500 ml");

        when(repo.save(any())).thenAnswer(invocationOnMock ->  invocationOnMock.getArgument(0));

        Medication response = service.create(1L, "Test medication", "Liquid", "500 ml");

        assertEquals("Test medication", response.getName());

        verify(repo, times(1)).save(any(Medication.class));
    }
}
