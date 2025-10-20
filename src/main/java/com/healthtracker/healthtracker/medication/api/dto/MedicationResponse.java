package com.healthtracker.healthtracker.medication.api.dto;

import com.healthtracker.healthtracker.medication.domain.Medication;

public record MedicationResponse(Long id, Long userId, String name, String form, String strength) {
    public static MedicationResponse from(Medication medication) {
        return new MedicationResponse(
                medication.getId(),
                medication.getUserId(),
                medication.getName(),
                medication.getForm(),
                medication.getStrength()
                );
    }
}
