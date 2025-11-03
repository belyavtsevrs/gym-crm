package com.epam.model.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Month;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MonthWorkload {
    private Month month;
    private Long workload = 0L;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthWorkload that = (MonthWorkload) o;
        return month == that.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(month);
    }
}
