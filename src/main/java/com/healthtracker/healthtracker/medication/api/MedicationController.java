package com.healthtracker.healthtracker.medication.api;

import com.healthtracker.healthtracker.medication.api.dto.CreateMedicationRequest;
import com.healthtracker.healthtracker.medication.api.dto.MedicationResponse;
import com.healthtracker.healthtracker.medication.app.MedicationService;
import com.healthtracker.healthtracker.medication.domain.Medication;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public ResponseEntity<List<MedicationResponse>> getMedications(@PathVariable  String userId) {
        List<Medication> meds = medicationService.getMedicationsByUserId(Long.parseLong(userId));
        return ResponseEntity.ok(meds.stream().map(MedicationResponse::from).toList());
    }

    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(@PathVariable Long userId, @RequestBody @Valid CreateMedicationRequest createMedicationRequest) {
        Medication m =  medicationService.create(userId, createMedicationRequest.name, createMedicationRequest.form, createMedicationRequest.strength);
        return ResponseEntity.ok(MedicationResponse.from(m));
    }
}
