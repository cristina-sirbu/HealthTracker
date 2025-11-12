package com.healthtracker.healthtracker.export;

import com.healthtracker.healthtracker.medication.domain.Medication;

import java.util.List;
import java.util.stream.Collectors;

public class CsvExportFormatter implements ExportFormatter {
    @Override
    public String format(List<?> data) {
        if (data == null || data.isEmpty()) return "";

        if (!(data.get(0) instanceof Medication m)) {
            throw new IllegalArgumentException("Expected list of Medication");
        }

        // header
        String header = "id,name,form,strength,userId,createdAt";

        // rows
        String rows = data.stream()
                .map(o -> (Medication) o)
                .map(med -> String.join(",",
                        String.valueOf(med.getId()),
                        med.getName(),
                        med.getForm(),
                        med.getStrength(),
                        String.valueOf(med.getUserId()),
                        String.valueOf(med.getCreatedAt())))
                .collect(Collectors.joining("\n"));

        return header + "\n" + rows;
    }
}
