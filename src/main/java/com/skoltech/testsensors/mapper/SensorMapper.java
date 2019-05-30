package com.skoltech.testsensors.mapper;

import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.repo.BuildingRepo;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorMapper extends AbstractMapper<Sensor, SensorDto> {
    private final ModelMapper modelMapper;
    private final BuildingRepo buildingRepo;

    @Autowired
    SensorMapper(ModelMapper modelMapper, BuildingRepo buildingRepo) {
        super(Sensor.class, SensorDto.class);
        this.modelMapper = modelMapper;
        this.buildingRepo = buildingRepo;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(Sensor.class, SensorDto.class)
                .addMappings(m -> m.skip(SensorDto::setBuildingId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(SensorDto.class, Sensor.class)
                .addMappings(m -> m.skip(Sensor::setBuilding)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Sensor source, SensorDto destination) {
        destination.setBuildingId(getId(source));
    }

    @Override
    void mapSpecificFields(SensorDto source, Sensor destination) {
        destination.setBuilding(buildingRepo.findById(source.getBuildingId()).orElse(null));
    }

    private Long getId(Sensor source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getBuilding().getId();
    }
}
