package com.epam.model.dto;

import java.time.Month;

public record MonthResponse(
        Month month,
        Integer workload
) {
}
