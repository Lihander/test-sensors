package com.skoltech.testsensors.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.dto.BuildingDto;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.domain.dto.SensorHistoryDto;
import com.skoltech.testsensors.mapper.SensorHistoryMapper;
import com.skoltech.testsensors.service.BuildingService;
import com.skoltech.testsensors.service.SensorHistoryService;
import com.skoltech.testsensors.service.SensorService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(SensorController.class)
public class SensorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorService sensorService;

    @MockBean
    private SensorHistoryService sensorHistoryService;

    @MockBean
    private BuildingService buildingService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<BuildingDto> buildings;
    private List<SensorDto> sensors;
    private List<SensorHistoryDto> sensorsHistory;

    @Before
    public void setUp() throws Exception {
        buildings = new ArrayList<>();
        sensors = new ArrayList<>();
        sensorsHistory =  new ArrayList<>();

        SensorHistoryDto sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(1L);
        sensorHistoryDto.setValue(-11);
        sensorHistoryDto.setDate(new Date(1));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(1L);
        sensorHistoryDto.setValue(13);
        sensorHistoryDto.setDate(new Date(2));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(1L);
        sensorHistoryDto.setValue(5);
        sensorHistoryDto.setDate(new Date(3));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(2L);
        sensorHistoryDto.setValue(-10);
        sensorHistoryDto.setDate(new Date(2));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(2L);
        sensorHistoryDto.setValue(22);
        sensorHistoryDto.setDate(new Date(4));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(3L);
        sensorHistoryDto.setValue(0);
        sensorHistoryDto.setDate(new Date(5));
        sensorsHistory.add(sensorHistoryDto);

        sensorHistoryDto = new SensorHistoryDto();
        sensorHistoryDto.setSensorId(3L);
        sensorHistoryDto.setValue(0);
        sensorHistoryDto.setDate(new Date(2));
        sensorsHistory.add(sensorHistoryDto);

        SensorDto sensor = new SensorDto();
        sensor.setId(1L);
        sensor.setCurrentValue(24);
        sensor.setBuildingId(1L);
        sensor.setSensorHistory(sensorsHistory.stream()
                .filter(streamSensorHistoryDto -> streamSensorHistoryDto.getSensorId() == 1L)
                .collect(Collectors.toList()));
        sensors.add(sensor);

        sensor = new SensorDto();
        sensor.setId(2L);
        sensor.setCurrentValue(-10);
        sensor.setBuildingId(1L);
        sensor.setSensorHistory(sensorsHistory.stream()
                .filter(streamSensorHistoryDto -> streamSensorHistoryDto.getSensorId() == 2L)
                .collect(Collectors.toList()));
        sensors.add(sensor);

        sensor = new SensorDto();
        sensor.setId(3L);
        sensor.setCurrentValue(18);
        sensor.setBuildingId(1L);
        sensor.setSensorHistory(sensorsHistory.stream()
                .filter(streamSensorHistoryDto -> streamSensorHistoryDto.getSensorId() == 3L)
                .collect(Collectors.toList()));
        sensors.add(sensor);

        BuildingDto building = new BuildingDto();
        building.setId(1L);
        building.setAddress("address 1");
        building.setSensors(Arrays.asList(sensors.get(0), sensors.get(2)));
        buildings.add(0, building);

        building = new BuildingDto();
        building.setAddress("address 2");
        building.setSensors(Collections.singletonList(sensors.get(1)));
        buildings.add(1, building);
    }

    @Test
    public void save() throws Exception {
        doNothing().when(sensorService).save(any());

        mockMvc.perform(post("/sensor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sensors.get(0))))
                .andExpect(status().isOk());
    }

    @Test
    public void getSensorBetweenDate() throws Exception {
        given(sensorHistoryService.getBySensorIdBetweenDates(any(), any(), any())).willReturn(sensorsHistory);

        mockMvc.perform(get("/sensor/getSensorBetweenDates")
                .param("sensorId", String.valueOf(sensorsHistory.get(0).getSensorId()))
                .param("startDate", String.valueOf(new Date(2).getTime()))
                .param("endDate", String.valueOf(new Date(4).getTime()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }

    @Test
    public void getCurrentValueByObjectId() throws Exception {
        Map<Long, Integer> expectedMap = new HashMap<>();
        expectedMap.put(sensors.get(0).getId(), sensors.get(0).getCurrentValue());
        expectedMap.put(sensors.get(1).getId(), sensors.get(1).getCurrentValue());

        given(buildingService.getCurrentValueByBuildingId(any())).willReturn(expectedMap);

        String response = mockMvc.perform(get("/sensor/getCurrentValueByBuildingId")
                .param("buildingId", String.valueOf(buildings.get(0).getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(response, objectMapper.writeValueAsString(expectedMap));
    }

    @Test
    public void getAll() throws Exception {
        Map<BuildingDto, Double> expectedMap = new HashMap<>();
        expectedMap.put(buildings.get(0), sensorsHistory.get(0).getValue().doubleValue());
        expectedMap.put(buildings.get(1), sensorsHistory.get(1).getValue().doubleValue());

        given(sensorHistoryService.getAll()).willReturn(expectedMap);

        String response = mockMvc.perform(get("/sensor/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(response, objectMapper.writeValueAsString(expectedMap));
    }
}