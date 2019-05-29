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
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/getSensorBetweenDates")
    public ResponseEntity getSensorBetweenDates(@RequestParam("sensor") String sensor,
                                          @RequestParam("startDate") String startDate,
                                          @RequestParam("endDate") String endDate) {
        List<SensorDto> sensorsDto = sensorService.getBySensorBetweenDates(sensor, startDate, endDate);
        return new ResponseEntity<>(sensorsDto, HttpStatus.OK);
    }

    @GetMapping("/getByObject")
    public ResponseEntity getByObject(@RequestParam("object") String object) {
        Map<String, Integer> response = sensorService.getByObject(object);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        Map<String, Map<String, Integer>> response = sensorService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
