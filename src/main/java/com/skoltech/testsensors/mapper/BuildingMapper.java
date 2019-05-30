package com.skoltech.testsensors.mapper;

import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.dto.BuildingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuildingMapper extends AbstractMapper<Building, BuildingDto> {

    @Autowired
    public BuildingMapper() {
        super(Building.class, BuildingDto.class);
    }

}
