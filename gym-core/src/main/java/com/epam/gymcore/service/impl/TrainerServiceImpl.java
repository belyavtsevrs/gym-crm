package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.service.TrainerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TrainerServiceImpl extends AbstractUserService<Trainer> implements TrainerService {

    protected TrainerServiceImpl(AbstractUserDao<Trainer> dao) {
        super(dao);
    }

}
