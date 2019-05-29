package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.exception.BadRequestException;
import com.skoltech.testsensors.repo.SensorRepo;
import com.skoltech.testsensors.service.SensorService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SensorServiceImpl implements SensorService {
    private final SensorRepo sensorRepo;
    private final ModelMapper modelMapper;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    public SensorServiceImpl(SensorRepo sensorRepo, ModelMapper modelMapper) {
        this.sensorRepo = sensorRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(SensorDto sensorDto) {
        sensorDto.setDate(new Date());
        Sensor sensor = modelMapper.map(sensorDto, Sensor.class);
        sensorRepo.save(sensor);
    }

    @Override
    public List<SensorDto> getBySensorBetweenDates(String sensor, String startDate, String endDate) {
        try {
            Date start = formatter.parse(startDate);
            Date end = formatter.parse(endDate);
            List<Sensor> sensors = sensorRepo.findBySensorBetweenDates(sensor, start, end);
            return sensors.stream().map(foundSensor ->
                    modelMapper.map(foundSensor, SensorDto.class))
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public Map<String, Integer> getByObject(String object) {
        List<Sensor> sensors = sensorRepo.findByObject(object);
        return sensors.stream()
                .collect(Collectors.toMap(Sensor::getSensor, Sensor::getValue, (sensor1, sensor2) -> sensor2));
    }

    @Override
    public Map<String, Map<String, Integer>> getAll() {
        Map<String, Map<String, Integer>> response = new HashMap<>();
        List<Sensor> sensors = sensorRepo.findAll();
        for (Sensor sensor : sensors) {
            String object = sensor.getObject();
            String sensorName = sensor.getSensor();
            Map<String, Integer> sensorsWithValue;
            if (response.containsKey(object)){
                sensorsWithValue = response.get(object);
                if (sensorsWithValue.containsKey(sensorName)) {
                    Integer value = sensorsWithValue.get(sensorName);
                    sensorsWithValue.put(sensorName, (value + sensor.getValue()) / 2);
                } else {
                    sensorsWithValue.put(sensorName, sensor.getValue());
                }
            } else {
                sensorsWithValue = new HashMap<>();
                sensorsWithValue.put(sensor.getSensor(), sensor.getValue());
            }
            response.put(object, sensorsWithValue);
        }
        return response;
    }
}
