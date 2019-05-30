package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.SensorHistory;
import com.skoltech.testsensors.domain.dto.BuildingDto;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import com.skoltech.testsensors.mapper.BuildingMapper;
import com.skoltech.testsensors.mapper.SensorHistoryMapper;
import com.skoltech.testsensors.mapper.SensorMapper;
import com.skoltech.testsensors.repo.SensorHistoryRepo;
import com.skoltech.testsensors.service.SensorHistoryService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorHistoryServiceImpl implements SensorHistoryService {
    private final SensorHistoryRepo sensorHistoryRepo;
    private final SensorMapper sensorMapper;
    private final SensorHistoryMapper sensorHistoryMapper;
    private final BuildingMapper buildingMapper;

    @Autowired
    public SensorHistoryServiceImpl(SensorMapper sensorMapper, SensorHistoryRepo sensorHistoryRepo, SensorHistoryMapper sensorHistoryMapper, BuildingMapper buildingMapper) {
        this.sensorMapper = sensorMapper;
        this.sensorHistoryRepo = sensorHistoryRepo;
        this.sensorHistoryMapper = sensorHistoryMapper;
        this.buildingMapper = buildingMapper;
    }

    @Override
    public void save(SensorDto sensorDto) {
        SensorHistory sensorHistory = new SensorHistory();
        sensorHistory.setValue(sensorDto.getCurrentValue());
        sensorHistory.setDate(new Date());
        sensorHistory.setSensor(sensorMapper.toEntity(sensorDto));
        sensorHistoryRepo.save(sensorHistory);
    }

    @Override
    public Map<BuildingDto, Double> getAll() {
        Map<BuildingDto, Double> response = new HashMap<>();
        List<SensorHistory> sensorsHistory = sensorHistoryRepo.findAll();
        for (SensorHistory sensorHistory : sensorsHistory) {
            BuildingDto building = buildingMapper.toDto(sensorHistory.getSensor().getBuilding());
            Integer value = sensorHistory.getValue();
            if (response.containsKey(building)) {
                Double currentAvgValue = response.get(building);
                response.put(building, (value == 0 && currentAvgValue == 0) ? 0 : (currentAvgValue + value) / 2);
            } else {
                response.put(building, value.doubleValue());
            }
        }
        return response;
    }

    @Override
    public List<SensorHistoryDto> getBySensorIdBetweenDates(Long id, Long startDate, Long endDate) {
        List<SensorHistory> sensors = sensorHistoryRepo.findBySensorIdBetweenDates(id, new Date(startDate), new Date(endDate));
        return sensors.stream().map(sensorHistoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
