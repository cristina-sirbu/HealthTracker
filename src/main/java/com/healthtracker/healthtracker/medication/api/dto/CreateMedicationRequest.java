package com.healthtracker.healthtracker.medication.api.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateMedicationRequest {
    @NotBlank
    public String name;
    public String form;
    public String strength;
}
