package com.epam.gymcore.service;

import com.epam.gymcore.domain.entity.Trainee;
import com.epam.gymcore.service.api.*;

public interface TraineeService extends
        Creator<Trainee>,
        Deleter<Long>,
        Retriever<Trainee,Long>,
        Updater<Trainee>,UserService<Trainee>
{

}
