package com.skoltech.testsensors.service;

import com.skoltech.testsensors.domain.dto.SensorDto;
import java.util.List;
import java.util.Map;

public interface SensorService {
    void save(SensorDto sensorDto);
    List<SensorDto> getBySensorBetweenDates(String sensor, String startDate, String endDate);
    Map<String, Integer>  getByObject(String object);
    Map<String, Map<String, Integer>> getAll();
}
