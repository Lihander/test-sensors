package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.SensorHistory;
import com.skoltech.testsensors.domain.dto.BuildingDto;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import com.skoltech.testsensors.mapper.BuildingMapper;
import com.skoltech.testsensors.mapper.SensorHistoryMapper;
import com.skoltech.testsensors.mapper.SensorMapper;
import com.skoltech.testsensors.repo.SensorHistoryRepo;
import com.skoltech.testsensors.service.SensorHistoryService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorHistoryServiceImplTest {
    @Autowired
    private SensorHistoryService sensorHistoryService;

    @MockBean
    private SensorMapper sensorMapper;

    @MockBean
    private SensorHistoryRepo sensorHistoryRepo;

    @MockBean
    private BuildingMapper buildingMapper;

    @MockBean
    private SensorHistoryMapper sensorHistoryMapper;

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

    }

    @Test
    public void save() {
        SensorDto sensorDto = new SensorDto();

        Sensor sensor = new Sensor();
        sensor.setId(sensors.get(0).getId());

        SensorHistory sensorHistory = new SensorHistory();
        when(sensorMapper.toEntity(sensorDto)).thenReturn(sensor);
        when(sensorHistoryRepo.save(sensorHistory)).thenReturn(sensorHistory);

        sensorHistoryService.save(sensorDto);
    }

    @Test
    public void getAll() {
        BuildingDto buildingDto= new BuildingDto();
        buildingDto.setId(buildings.get(0).getId());
        buildingDto.setAddress(buildings.get(0).getAddress());

        BuildingDto buildingDto1 = new BuildingDto();
        buildingDto1.setId(buildings.get(1).getId());
        buildingDto1.setAddress(buildings.get(1).getAddress());

        when(sensorHistoryRepo.findAll()).thenReturn(sensorsHistory);
        when(buildingMapper.toDto(buildings.get(0))).thenReturn(buildingDto);
        when(buildingMapper.toDto(buildings.get(1))).thenReturn(buildingDto1);

        Map<BuildingDto, Double> found = sensorHistoryService.getAll();

        assertThat(found).hasSize(2).containsKeys(buildingDto, buildingDto1);

        assertThat(found.get(buildingDto)).isEqualTo(9.25);
        assertThat(found.get(buildingDto1)).isEqualTo(0);
    }

    @Test
    public void getBySensorIdBetweenDates() {
        when(sensorHistoryRepo.findBySensorIdBetweenDates(1L, new Date(1), new Date(3))).thenReturn(
                Arrays.asList(sensorsHistory.get(0), sensorsHistory.get(1), sensorsHistory.get(3)));

        SensorHistoryDto sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setValue(sensorsHistory.get(0).getValue());
        when(sensorHistoryMapper.toDto(sensorsHistory.get(0))).thenReturn(sensorHistoryDto);

        when(sensorHistoryMapper.toDto(sensorsHistory.get(1))).thenReturn(new SensorHistoryDto());
        when(sensorHistoryMapper.toDto(sensorsHistory.get(3))).thenReturn(new SensorHistoryDto());

        List<SensorHistoryDto> found = sensorHistoryService.getBySensorIdBetweenDates(1L, new Date(1).getTime(), new Date(3).getTime());

        assertThat(found).hasSize(3);
        assertThat(found.get(0).getValue()).isEqualTo(sensorHistoryDto.getValue());
    }
}