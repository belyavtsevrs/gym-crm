package com.epam.gymcore.service.Impl;

import com.epam.gymcore.dao.TraineeDaoImpl;
import com.epam.gymcore.domain.entity.TraineeImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class TraineeServiceImpl extends AbstractService<Trainee, TraineeDaoImpl> implements TraineeService {

    public TraineeServiceImpl(TraineeDaoImpl dao) {
        super(dao);
    }

    @Override
    public Trainee create(Trainee entity) {
        String username = UserUtil.createUsername(entity,findAll());
        String password = entity.getPassword();
        
        Trainee trainee = new TraineeImpl(
                entity.getFirstName(),
                entity.getLastName(),
                username,
                password,
                true,
                LocalDate.now(),
                entity.getAddress()
        );
        dao.save(trainee);
        log.info("trainee = {} has been created",trainee);
        return trainee;
    }

    @Override
    public Trainee update(Trainee entity) {
        Optional<Trainee> optionalTrainee = dao.findById(entity.getId());
        if (optionalTrainee.isEmpty()) {
            throw new IllegalArgumentException("Trainee with such id not found");
        }

        Trainee current = optionalTrainee.get();
        String username = UserUtil.createUsername(entity,findAll());
        Trainee updatedTrainee = new TraineeImpl(
                entity.getFirstName(),
                entity.getLastName(),
                username,
                entity.getPassword(),
                entity.isActive(),
                current.getDateBirth(),
                entity.getAddress()
        );

        dao.save(updatedTrainee);
        log.info("trainee = {} has been updated", updatedTrainee);
        return updatedTrainee;
    }
}
