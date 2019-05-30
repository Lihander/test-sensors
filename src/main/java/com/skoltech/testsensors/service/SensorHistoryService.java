package com.skoltech.testsensors.service;

import com.skoltech.testsensors.domain.dto.BuildingDto;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import java.util.List;
import java.util.Map;

public interface SensorHistoryService {
    void save(SensorDto sensorDto);
    Map<BuildingDto, Double> getAll();
    List<SensorHistoryDto> getBySensorIdBetweenDates(Long id, Long startDate, Long endDate);
}
