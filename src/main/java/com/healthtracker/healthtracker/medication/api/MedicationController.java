package com.healthtracker.healthtracker.medication.api;

import com.healthtracker.healthtracker.export.ExportService;
import com.healthtracker.healthtracker.medication.api.dto.CreateMedicationRequest;
import com.healthtracker.healthtracker.medication.api.dto.MedicationPageResponse;
import com.healthtracker.healthtracker.medication.api.dto.MedicationResponse;
import com.healthtracker.healthtracker.medication.app.MedicationService;
import com.healthtracker.healthtracker.medication.domain.Medication;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/medications")
public class MedicationController {

    private final MedicationService medicationService;
    private final ExportService exportService;

    public MedicationController(MedicationService medicationService, ExportService exportService) {
        this.medicationService = medicationService;
        this.exportService = exportService;
    }

    @GetMapping
    public ResponseEntity<MedicationPageResponse> getMedications(
            @PathVariable  Long userId,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        if (limit <= 0 || limit > 100 || offset < 0) {
            return ResponseEntity.badRequest().build();
        }

        Page<Medication> page = medicationService.getMedicationByUserId(userId, limit, offset);
        List<MedicationResponse> items = page.getContent()
                .stream()
                .map(MedicationResponse::from)
                .toList();

        MedicationPageResponse body = new MedicationPageResponse(
                items,
                page.getTotalElements(),
                limit,
                offset
        );
        return ResponseEntity.ok(body);

//        List<Medication> meds = medicationService.getMedicationsByUserId(userId);
//        return ResponseEntity.ok(meds.stream().map(MedicationResponse::from).toList());
    }

    @PostMapping
    public ResponseEntity<MedicationResponse> createMedication(@PathVariable Long userId, @RequestBody @Valid CreateMedicationRequest createMedicationRequest) {
        Medication m =  medicationService.create(userId, createMedicationRequest.name, createMedicationRequest.form, createMedicationRequest.strength);
        return ResponseEntity.ok(MedicationResponse.from(m));
    }

    @GetMapping("/exports")
    public ResponseEntity<String> exportMedications(@PathVariable Long userId,
                                                    @RequestParam(defaultValue = "json") String format) {
        String output = exportService.exportMedications(userId, format);
        String contentType = format.equalsIgnoreCase("json") ? "application/json" : "text/csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(output);
    }
}
