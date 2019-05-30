package com.skoltech.testsensors.mapper;

import com.skoltech.testsensors.domain.SensorHistory;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import com.skoltech.testsensors.repo.SensorRepo;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SensorHistoryMapper extends AbstractMapper<SensorHistory, SensorHistoryDto>{
    private final ModelMapper modelMapper;
    private final SensorRepo sensorRepo;

    @Autowired
    public SensorHistoryMapper(ModelMapper modelMapper, SensorRepo sensorRepo) {
        super(SensorHistory.class, SensorHistoryDto.class);
        this.modelMapper = modelMapper;
        this.sensorRepo = sensorRepo;
    }

    @PostConstruct
    public void setupMapper() {
        modelMapper.createTypeMap(SensorHistory.class, SensorHistoryDto.class)
                .addMappings(m -> m.skip(SensorHistoryDto::setSensorId)).setPostConverter(toDtoConverter());
        modelMapper.createTypeMap(SensorHistoryDto.class, SensorHistory.class)
                .addMappings(m -> m.skip(SensorHistory::setSensor)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(SensorHistory source, SensorHistoryDto destination) {
        destination.setSensorId(getId(source));
    }

    private Long getId(SensorHistory source) {
        return Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getSensor().getId();
    }

    @Override
    void mapSpecificFields(SensorHistoryDto source, SensorHistory destination) {
        destination.setSensor(sensorRepo.findById(source.getSensorId()).orElse(null));
    }
}
