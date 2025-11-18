package com.healthtracker.healthtracker.medication.api.dto;

import java.util.List;

public record MedicationPageResponse(
        List<MedicationResponse> items,
        long count,
        int limit,
        int offset
) {}
