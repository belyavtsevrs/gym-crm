package com.epam.gymcore.service.Impl;

import com.epam.gymcore.dao.TraineeDaoImpl;
import com.epam.gymcore.dao.TrainerDaoImpl;
import com.epam.gymcore.domain.entity.TraineeImpl;
import com.epam.gymcore.domain.entity.TrainerImpl;
import com.epam.gymcore.domain.model.Trainee;
import com.epam.gymcore.domain.model.Trainer;
import com.epam.gymcore.service.TrainerService;
import com.epam.gymcore.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TrainerServiceImpl extends AbstractService<Trainer, TrainerDaoImpl> implements TrainerService {

    public TrainerServiceImpl(TrainerDaoImpl dao) {
        super(dao);
    }

    @Override
    public Trainer create(Trainer entity) {
        String username = UserUtil.createUsername(entity, findAll());
        String password = entity.getPassword();

        Trainer trainer = new TrainerImpl(
                entity.getFirstName(),
                entity.getLastName(),
                username,
                password,
                true,
                entity.getSpecialization()
        );

        dao.save(trainer);
        log.info("trainer = {} has been created", trainer);

        return trainer;
    }

    @Override
    public List<Trainer> findAll() {
        return dao.findAll();
    }

    @Override
    public Optional<Trainer> findById(Long aLong) {
        return dao.findById(aLong);
    }

    @Override
    public Trainer update(Trainer entity) {
        Optional<Trainer> optionalTrainee = dao.findById(entity.getId());
        if (optionalTrainee.isEmpty()) {
            throw new IllegalArgumentException("Trainee with such id not found");
        }

        String username = UserUtil.createUsername(entity,findAll());
        String password = entity.getPassword();

        Trainer trainer = new TrainerImpl(
                entity.getFirstName(),
                entity.getLastName(),
                username,
                password,
                true,
                entity.getSpecialization()
        );

        dao.save(trainer);
        log.info("trainer = {} has been created",trainer);
        return trainer;
    }
}
