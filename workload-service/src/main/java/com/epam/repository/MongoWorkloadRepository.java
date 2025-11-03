package com.epam.repository;

import com.epam.model.entity.mongo.Workload;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MongoWorkloadRepository extends MongoRepository<Workload,String> {
    Optional<Workload> findByTrainerUsername(String trainerUsername);
}
