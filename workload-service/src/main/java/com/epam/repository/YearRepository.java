package com.epam.repository;

import com.epam.model.entity.TrainerWorkload;
import com.epam.model.entity.Years;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YearRepository extends JpaRepository<Years,Long> {
    Years findYearByWorkloadYearAndTrainerWorkload(Integer workloadYear, TrainerWorkload trainerWorkload);
}
