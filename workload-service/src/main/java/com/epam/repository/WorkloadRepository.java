package com.epam.repository;

import com.epam.model.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface WorkloadRepository extends JpaRepository<TrainerWorkload,Long> {

}
