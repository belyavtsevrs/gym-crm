package com.epam.model.mapper;

import com.epam.model.dto.TrainerWorkloadRequest;
import com.epam.model.entity.TrainerWorkload;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkloadReqMapper {
    TrainerWorkload toTrainerWorkload(TrainerWorkloadRequest wr);
}
