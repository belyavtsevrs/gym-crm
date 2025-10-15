package com.epam.repository;

import com.epam.model.entity.Months;
import com.epam.model.entity.Years;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Month;

public interface MonthRepository extends JpaRepository<Months,Long> {
    Months findMonthsByMonthAndYearsRef(Month month, Years years);
}
