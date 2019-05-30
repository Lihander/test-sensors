package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.SensorHistory;
import com.skoltech.testsensors.domain.dto.BuildingDto;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import com.skoltech.testsensors.repo.BuildingRepo;
import com.skoltech.testsensors.service.BuildingService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildingServiceImplTest {

    @Autowired
    private BuildingService buildingService;

    @MockBean
    private BuildingRepo buildingRepo;

    private List<Building> buildings;
    private List<Sensor> sensors;
    private List<SensorHistory> sensorsHistory;

    @Before
    public void setUp() throws Exception {
        buildings = new ArrayList<>();
        sensors = new ArrayList<>();
        sensorsHistory =  new ArrayList<>();

        //1 здание
        Building building = new Building();
        building.setId(1L);
        building.setAddress("address 1");

        Sensor sensor = new Sensor();
        sensor.setId(1L);
        sensor.setCurrentValue(24);
        sensor.setBuilding(building);

        SensorHistory sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(-11);
        sensorHistoryDto.setDate(new Date(1));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(13);
        sensorHistoryDto.setDate(new Date(2));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(5);
        sensorHistoryDto.setDate(new Date(3));
        sensorsHistory.add(sensorHistoryDto);

        sensor.setSensorHistory(sensorsHistory.stream()
                .filter(streamSensorHistory -> streamSensorHistory.getSensor().getId() == 1L)
                .collect(Collectors.toList()));
        sensors.add(sensor);

        sensor = new Sensor();
        sensor.setId(2L);
        sensor.setCurrentValue(-10);
        sensor.setBuilding(building);

        sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(-10);
        sensorHistoryDto.setDate(new Date(2));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(22);
        sensorHistoryDto.setDate(new Date(4));
        sensorsHistory.add(sensorHistoryDto);

        sensor.setSensorHistory(sensorsHistory.stream()
                .filter(streamSensorHistory -> streamSensorHistory.getSensor().getId() == 2L)
                .collect(Collectors.toList()));
        sensors.add(sensor);

        building.setSensors(Arrays.asList(sensors.get(0), sensors.get(1)));
        buildings.add(0, building);

        //2 здание
        building = new Building();
        building.setAddress("address 2");

        sensor = new Sensor();
        sensor.setId(3L);
        sensor.setCurrentValue(18);
        sensor.setBuilding(building);

        sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(0);
        sensorHistoryDto.setDate(new Date(5));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistory();
        sensorHistoryDto.setSensor(sensor);
        sensorHistoryDto.setValue(0);
        sensorHistoryDto.setDate(new Date(2));
        sensorsHistory.add(sensorHistoryDto);

        sensor.setSensorHistory(sensorsHistory.stream()
                .filter(streamSensorHistory -> streamSensorHistory.getSensor().getId() == 3L)
                .collect(Collectors.toList()));
        sensors.add(sensor);

        building.setSensors(Collections.singletonList(sensors.get(2)));
        buildings.add(1, building);

        Long testId = 1L;
        when(buildingRepo.getOne(testId)).thenReturn(buildings.get(0));
    }

    @Test
    public void getCurrentValueByBuildingId() {
        Map<Long, Integer> found = buildingService.getCurrentValueByBuildingId(1L);

        assertThat(found)
                .hasSize(2)
                .containsKeys(sensors.get(0).getId(),  sensors.get(1).getId())
                .doesNotContainKeys(sensors.get(2).getId());

        assertThat(found.get(sensors.get(0).getId())).isEqualTo(sensors.get(0).getCurrentValue());
        assertThat(found.get(sensors.get(1).getId())).isEqualTo(sensors.get(1).getCurrentValue());
    }
}