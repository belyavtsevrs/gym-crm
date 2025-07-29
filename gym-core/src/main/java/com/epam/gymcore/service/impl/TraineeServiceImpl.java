package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TraineeDao;
import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.TraineeService;
import com.epam.gymcore.service.UserService;
import com.epam.gymcore.util.UserUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TraineeServiceImpl extends AbstractUserService<Trainee> implements TraineeService {

    protected TraineeServiceImpl(AbstractUserDao<Trainee> dao) {
        super(dao);
    }

}
