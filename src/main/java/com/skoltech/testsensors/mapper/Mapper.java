package com.skoltech.testsensors.mapper;

import com.skoltech.testsensors.domain.AbstractEntity;
import com.skoltech.testsensors.domain.dto.AbstractDto;

public interface Mapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);

    D toDto(E entity);
}
