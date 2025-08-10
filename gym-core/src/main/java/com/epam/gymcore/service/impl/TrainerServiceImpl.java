package com.epam.gymcore.service.impl;

import com.epam.gymcore.dao.AbstractUserDao;
import com.epam.gymcore.dao.TrainingTypeDao;
import com.epam.gymcore.domain.dto.TrainerRegistrationDto;
import com.epam.gymcore.domain.dto.UserDto;
import com.epam.gymcore.domain.entity.Trainer;
import com.epam.gymcore.domain.entity.TrainingType;
import com.epam.gymcore.domain.mapper.TrainerMapper;
import com.epam.gymcore.domain.mapper.TrainingTypeMapper;
import com.epam.gymcore.service.TrainerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TrainerServiceImpl extends AbstractUserService<Trainer> implements TrainerService {
    private final TrainingTypeDao trainingTypeDao;
    private final TrainerMapper trainerMapper;
    private final TrainingTypeMapper trainingTypeMapper;

    protected TrainerServiceImpl(AbstractUserDao<Trainer> dao,
                                 TrainingTypeDao trainingTypeDao,
                                 TrainerMapper trainerMapper,
                                 TrainingTypeMapper trainingTypeMapper) {
        super(dao);
        this.trainingTypeDao = trainingTypeDao;
        this.trainerMapper = trainerMapper;
        this.trainingTypeMapper = trainingTypeMapper;
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

}
