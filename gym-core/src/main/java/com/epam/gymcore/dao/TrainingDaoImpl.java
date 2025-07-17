package com.epam.gymcore.dao;

import com.epam.gymcore.dao.storage.InMemoryStorage;
import com.epam.gymcore.domain.model.Training;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TrainingDaoImpl extends AbstractDao<Training>{

    public TrainingDaoImpl(InMemoryStorage<Training> storage) {
        super(storage);
    }

    public List<Training> getTrainingByTrainee(Long traineeId){
        return storage.getStorage().values().stream()
                .filter(x-> Objects.equals(x.getTraineeId(),traineeId))
                .collect(Collectors.toList());
    };

    public List<Training> getTrainingByTrainer(Long trainerId){
        return storage.getStorage().values().stream()
                .filter(x->Objects.equals(x.getTrainerId(),trainerId))
                .collect(Collectors.toList());
    };
}
