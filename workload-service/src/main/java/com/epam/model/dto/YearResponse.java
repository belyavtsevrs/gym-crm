package com.epam.model.dto;

import java.io.Serializable;
import java.time.Month;
import java.util.List;

public record YearResponse(
        Integer year,
        List<MonthResponse> months
) implements Serializable {
}
