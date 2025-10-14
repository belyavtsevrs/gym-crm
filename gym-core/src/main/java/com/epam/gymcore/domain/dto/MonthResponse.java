package com.epam.gymcore.domain.dto;

import java.time.Month;

public record MonthResponse(
        Month month,
        Integer workload
) {
}
