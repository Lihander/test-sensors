package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.mapper.SensorMapper;
import com.skoltech.testsensors.repo.SensorRepo;
import com.skoltech.testsensors.service.SensorHistoryService;
import com.skoltech.testsensors.service.SensorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.doNothing;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceImplTest {

    @Autowired
    private SensorService sensorService;

    @MockBean
    private SensorRepo sensorRepo;

    @MockBean
    private SensorMapper sensorMapper;

    @MockBean
    private SensorHistoryService sensorHistoryService;

    private SensorDto sensorDto;

    @Before
    public void setUp() throws Exception {
        Sensor sensor = new Sensor();
        sensor.setId(1L);

        sensorDto = new SensorDto();
        sensorDto.setId(1L);

        Mockito.when(sensorMapper.toEntity(sensorDto)).thenReturn(sensor);
        Mockito.when(sensorRepo.save(sensor)).thenReturn(sensor);
        doNothing().when(sensorHistoryService).save(sensorDto);
    }

    @Test
    public void save() {
        sensorService.save(sensorDto);
    }
}