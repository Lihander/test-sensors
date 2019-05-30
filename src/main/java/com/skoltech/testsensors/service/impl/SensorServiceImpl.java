package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.mapper.SensorMapper;
import com.skoltech.testsensors.repo.SensorRepo;
import com.skoltech.testsensors.service.SensorHistoryService;
import com.skoltech.testsensors.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorServiceImpl implements SensorService {
    private final SensorRepo sensorRepo;
    private final SensorMapper sensorMapper;
    private final SensorHistoryService sensorHistoryService;

    @Autowired
    public SensorServiceImpl(SensorRepo sensorRepo, SensorMapper sensorMapper, SensorHistoryService sensorHistoryService) {
        this.sensorRepo = sensorRepo;
        this.sensorMapper = sensorMapper;
        this.sensorHistoryService = sensorHistoryService;
    }

    @Override
    public void save(SensorDto sensorDto) {
        Sensor sensor = sensorMapper.toEntity(sensorDto);
        sensorRepo.save(sensor);
        sensorHistoryService.save(sensorDto);
    }

}
