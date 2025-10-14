package com.epam.gymcore.domain.dto;


import java.util.List;

public record YearResponse(
        Integer year,
        List<MonthResponse> months
) {
}
