package com.skoltech.testsensors.service;

import com.skoltech.testsensors.domain.dto.SensorDto;
import java.util.List;
import java.util.Map;

public interface SensorService {
    void save(SensorDto sensorDto);
    List<SensorDto> getByIdBetweenDates(String id, String startDate, String endDate);
    SensorDto getByObject(String object);
    Map<String, Integer> getAll();
}
