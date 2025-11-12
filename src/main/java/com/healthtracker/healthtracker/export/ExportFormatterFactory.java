package com.healthtracker.healthtracker.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ExportFormatterFactory {

    private final ObjectMapper objectMapper;

    public ExportFormatterFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ExportFormatter getFormatter(String formatType) {
        return switch (formatType.toLowerCase()) {
            case "json" -> new JsonExportFormatter(objectMapper);
            case "csv" -> new CsvExportFormatter();
            default -> throw new IllegalArgumentException("Unsupported format: "+formatType);
        };
    }
}
