package com.epam.gymcore.domain.entity;

import com.epam.gymcore.domain.model.TrainingType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class TrainingTypeImpl implements TrainingType {
    private Long id;
    private String name;

    @Override
    public void setId(Long aLong) {
        this.id = aLong;
    }

    @JsonCreator
    public TrainingTypeImpl(
            @JsonProperty("id") Long id,
            @JsonProperty("name") String name
    ) {
        this.id = id;
        this.name = name;
    }
}
