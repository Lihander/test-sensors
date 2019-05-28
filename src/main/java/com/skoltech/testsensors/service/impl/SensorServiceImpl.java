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

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

    @Autowired
    public SensorServiceImpl(SensorRepo sensorRepo, ModelMapper modelMapper) {
        this.sensorRepo = sensorRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(SensorDto sensorDto) {
        Sensor sensor = modelMapper.map(sensorDto, Sensor.class);
        sensorRepo.save(sensor);
        modelMapper.map(sensor, SensorDto.class);
    }

    @Override
    public List<SensorDto> getByIdBetweenDates(String id, String startDate, String endDate) {
        try {
            Date start = formatter.parse(startDate);
            Date end = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
            List<Sensor> sensors = sensorRepo.findByIdBetweenDates(Long.parseLong(id), start, end);
            return sensors.stream().map(sensor ->
                    modelMapper.map(sensor, SensorDto.class))
                    .collect(Collectors.toList());
        } catch (ParseException e) {
            throw new BadRequestException();
        }
    }

    @Override
    public SensorDto getByObject(String object) {
        Sensor sensor = sensorRepo.findByObject(object);
        return modelMapper.map(sensor, SensorDto.class);
    }

    @Override
    public Map<String, Integer> getAll() {
        List<Sensor> sensors = sensorRepo.findAll();
        return sensors.stream().collect(Collectors.toMap(Sensor::getObject, Sensor::getValue));
    }
}
