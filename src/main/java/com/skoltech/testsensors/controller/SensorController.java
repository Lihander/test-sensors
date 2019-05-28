package com.skoltech.testsensors.controller;

import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.service.SensorService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sensor")
public class SensorController {
    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody SensorDto sensorDto) {
        sensorService.save(sensorDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}/{startDate}-{endDate}")
    public ResponseEntity getSensorBetweenDate(@PathVariable String id,
                                          @PathVariable String startDate,
                                          @PathVariable String endDate) {
        List<SensorDto> sensorsDto = sensorService.getByIdBetweenDates(id, startDate, endDate);
        return new ResponseEntity<>(sensorsDto, HttpStatus.OK);
    }

    @GetMapping("getByObject/{object}")
    public ResponseEntity getByObject(@PathVariable String object) {
        SensorDto sensorDto = sensorService.getByObject(object);
        return new ResponseEntity<>(sensorDto, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        Map<String, Integer > response = sensorService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
