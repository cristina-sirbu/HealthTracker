package com.healthtracker.healthtracker.export;

import java.util.List;

public interface ExportFormatter {
    String format(List<?> data);
}
