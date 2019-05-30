package com.skoltech.testsensors.controller;

import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.dto.BuildingDto;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import com.skoltech.testsensors.service.BuildingService;
import com.skoltech.testsensors.service.SensorHistoryService;
import com.skoltech.testsensors.service.SensorService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sensor")
public class SensorController {
    private final BuildingService buildingService;
    private final SensorService sensorService;
    private final SensorHistoryService sensorHistoryService;

    @Autowired
    public SensorController(SensorService sensorService, SensorHistoryService sensorHistoryService, BuildingService buildingService) {
        this.sensorService = sensorService;
        this.sensorHistoryService = sensorHistoryService;
        this.buildingService = buildingService;
    }

    @PostMapping
    public ResponseEntity save(@RequestBody SensorDto sensorDto) {
        sensorService.save(sensorDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getSensorBetweenDates")
    public ResponseEntity getSensorBetweenDates(@RequestParam("sensorId") Long id,
                                          @RequestParam("startDate") Long startDate,
                                          @RequestParam("endDate") Long endDate) {
        List<SensorHistoryDto> sensorsDto = sensorHistoryService.getBySensorIdBetweenDates(id, startDate, endDate);
        return new ResponseEntity<>(sensorsDto, HttpStatus.OK);
    }

    @GetMapping("/getCurrentValueByBuildingId")
    public ResponseEntity getCurrentValueByObjectId(@RequestParam("buildingId") Long id) {
        Map<Long, Integer> response = buildingService.getCurrentValueByBuildingId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity getAll() {
        Map<BuildingDto, Double> response = sensorHistoryService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
