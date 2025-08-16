package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TrainingTypeDao;
import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.dto.TrainingDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.Training;
import com.epam.gymcore.domain.entity.TrainingType;
import com.epam.gymcore.domain.exception.UserNotFoundException;
import com.epam.gymcore.domain.mapper.TrainerMapper;
import com.epam.gymcore.domain.mapper.TrainingMapper;
import com.epam.gymcore.domain.mapper.TrainingTypeMapper;
import com.epam.gymcore.service.TrainerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TrainerServiceImpl extends AbstractUserService<Trainer> implements TrainerService {
    private final TrainingTypeDao trainingTypeDao;
    private final TrainerMapper trainerMapper;
    private final TrainingTypeMapper trainingTypeMapper;
    private final TrainingMapper trainingMapper;

    protected TrainerServiceImpl(AbstractUserDao<Trainer> dao,
                                 TrainingTypeDao trainingTypeDao,
                                 TrainerMapper trainerMapper,
                                 TrainingTypeMapper trainingTypeMapper, TrainingMapper trainingMapper) {
        super(dao);
        this.trainingTypeDao = trainingTypeDao;
        this.trainerMapper = trainerMapper;
        this.trainingTypeMapper = trainingTypeMapper;
        this.trainingMapper = trainingMapper;
    }

    @Transactional
    public UserDto register(TrainerRegistrationDto dto) {
        Trainer trainer = trainerMapper.toEntity(dto);
        log.info("(TrainerServiceImpl) newTrainer after mapping = {}", trainer);

        if (dto.getSpecialization() != null && !dto.getSpecialization().isEmpty()) {

            Set<TrainingType> types = dto.getSpecialization().stream()
                    .map(s -> Optional.ofNullable(s.name())
                            .map(String::trim)
                            .filter(n -> !n.isEmpty())
                            .orElseThrow(() -> new IllegalArgumentException("Empty training type name")))
                    .map(n -> n.toUpperCase(Locale.ROOT))
                    .map(n -> trainingTypeDao.findByName(n)
                            .orElseGet(() -> trainingTypeDao.save(new TrainingType(n))))
                    .collect(Collectors.toSet());


            trainer.setSpecializations(types);
        }

        Trainer saved = super.create(trainer);
        log.info("(TrainerServiceImpl) new trainer is registered = {}", saved);

        return new UserDto(saved.getFirstName(), saved.getLastName());
    }

    @Override
    public List<TrainingDto> trainerTrainingsList(String username, LocalDateTime from, LocalDateTime to, String traineeName) {
        List<TrainingDto> trainingDtoList = new ArrayList<>();
        List<Training> trainings;
        if(from != null && to != null){
            trainings = dao.getUsersByUsernameAndCriteria(username,from,to);
            log.info("trainings by criteria = {}",trainings);

            for(var x : trainings){
                var trainee = x.getTrainee();
                var type = x.getTrainingType();
                var res = trainingMapper.toDto(x,type,trainee);
                trainingDtoList.add(res);
            }
            if((traineeName != null && !traineeName.isBlank())){
                return trainingDtoList.stream()
                        .filter(x->x.getTraineeName().equals(traineeName))
                        .toList();
            }
            return trainingDtoList;
        }
        Trainer trainer = dao.findByUsername(username).orElseThrow(() ->
                new UserNotFoundException(String.format("User with username %s not found", username))
        );
        log.info("trainer = {}",trainer);

        for(var x : trainer.getTrainings()){
            var trainee = x.getTrainee();
            var type = x.getTrainingType();
            var res = trainingMapper.toDto(x,type,trainee);
            trainingDtoList.add(res);
        }
        return trainingDtoList;
    }

}
