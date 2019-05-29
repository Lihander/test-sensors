package com.skoltech.testsensors.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.service.SensorService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private ObjectMapper objectMapper;

    private List<SensorDto> sensors;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Before
    public void setUp() throws Exception {
        sensors = new ArrayList<>();
        SensorDto sensor = new SensorDto();
        sensor.setSensor("Test sensor 1");
        sensor.setObject("Test object 1");
        sensor.setDate(formatter.parse("28-05-2019"));
        sensor.setValue(24);
        sensors.add(0, sensor);

        sensor = new SensorDto();
        sensor.setSensor("Test sensor 2");
        sensor.setObject("Test object 2");
        sensor.setDate(formatter.parse("27-05-2019"));
        sensor.setValue(30);
        sensors.add(1, sensor);

        sensor = new SensorDto();
        sensor.setSensor("Test sensor 1");
        sensor.setObject("Test object 3");
        sensor.setDate(formatter.parse("11-05-2019"));
        sensor.setValue(11);
        sensors.add(2, sensor);

        sensor = new SensorDto();
        sensor.setSensor("Test sensor 3");
        sensor.setObject("Test object 1");
        sensor.setDate(formatter.parse("31-05-2019"));
        sensor.setValue(-5);
        sensors.add(3, sensor);
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
        given(sensorService.getBySensorBetweenDates(any(), any(), any())).willReturn(sensors);

        mockMvc.perform(get("/sensor/getSensorBetweenDates")
                .param("sensor", sensors.get(0).getSensor())
                .param("startDate","26-05-2019")
                .param("endDate","31-05-2019")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].sensor", is(sensors.get(0).getSensor())));
    }

    @Test
    public void getByObject() throws Exception {
        Map<String, Integer> expectedMap = new HashMap<>();
        expectedMap.put(sensors.get(0).getSensor(), sensors.get(0).getValue());
        expectedMap.put(sensors.get(1).getSensor(), sensors.get(1).getValue());

        given(sensorService.getByObject(any())).willReturn(expectedMap);

        String response = mockMvc.perform(get("/sensor/getByObject")
                .param("object", sensors.get(0).getObject())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(response, objectMapper.writeValueAsString(expectedMap));
    }

    @Test
    public void getAll() throws Exception {
        Map<String, Map<String, Integer>> expectedMap = new HashMap<>();
        Map<String, Integer> sensorsWithValue = new HashMap<>();
        sensorsWithValue.put(sensors.get(0).getSensor(), sensors.get(0).getValue());
        sensorsWithValue.put(sensors.get(3).getSensor(), sensors.get(3).getValue());
        expectedMap.put(sensors.get(0).getObject(), sensorsWithValue);

        given(sensorService.getAll()).willReturn(expectedMap);

        String response = mockMvc.perform(get("/sensor/getAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assert.assertEquals(response, objectMapper.writeValueAsString(expectedMap));
    }
}