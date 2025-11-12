package com.healthtracker.healthtracker.export;

import com.healthtracker.healthtracker.medication.app.MedicationService;
import com.healthtracker.healthtracker.medication.domain.Medication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {

    private final MedicationService medicationService;
    private final ExportFormatterFactory exportFormatterFactory;

    public ExportService(MedicationService medicationService, ExportFormatterFactory exportFormatterFactory) {
        this.medicationService = medicationService;
        this.exportFormatterFactory = exportFormatterFactory;
    }

    public String exportMedications(Long userId, String format) {
        List<Medication> medicationList = medicationService.getMedicationsByUserId(userId);
        ExportFormatter formatter = exportFormatterFactory.getFormatter(format);
        return formatter.format(medicationList);
    }
}
