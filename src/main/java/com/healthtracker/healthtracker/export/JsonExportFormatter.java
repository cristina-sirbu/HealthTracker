package com.healthtracker.healthtracker.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonExportFormatter implements ExportFormatter{

    private final ObjectMapper mapper;
    public JsonExportFormatter(ObjectMapper mapper) { this.mapper = mapper; }

    @Override
    public String format(List<?> data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
