package com.epam.model.dto;

import java.io.Serializable;
import java.time.Month;

public record MonthResponse (
        Month month,
        Integer workload
) implements Serializable  {
}
