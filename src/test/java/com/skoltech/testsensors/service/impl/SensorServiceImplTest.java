package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.domain.dto.SensorDto;
import com.skoltech.testsensors.repo.SensorRepo;
import com.skoltech.testsensors.service.SensorService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SensorServiceImplTest {

    @Autowired
    private SensorService sensorService;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private SensorRepo sensorRepo;

    private List<SensorDto> sensorsDto;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Before
    public void setUp() throws Exception {
        sensorsDto = new ArrayList<>();
        SensorDto sensorDto = new SensorDto();
        sensorDto.setId(1L);
        sensorDto.setSensor("Test sensor 1");
        sensorDto.setObject("Test object 1");
        sensorDto.setDate(formatter.parse("28-05-2019"));
        sensorDto.setValue(24);
        sensorsDto.add(0, sensorDto);

        sensorDto = new SensorDto();
        sensorDto.setId(2L);
        sensorDto.setSensor("Test sensor 2");
        sensorDto.setObject("Test object 2");
        sensorDto.setDate(formatter.parse("28-05-2019"));
        sensorDto.setValue(30);
        sensorsDto.add(1, sensorDto);

        sensorDto = new SensorDto();
        sensorDto.setId(3L);
        sensorDto.setSensor("Test sensor 1");
        sensorDto.setObject("Test object 3");
        sensorDto.setDate(formatter.parse("29-05-2019"));
        sensorDto.setValue(11);
        sensorsDto.add(2, sensorDto);

        sensorDto = new SensorDto();
        sensorDto.setId(4L);
        sensorDto.setSensor("Test sensor 3");
        sensorDto.setObject("Test object 1");
        sensorDto.setDate(formatter.parse("31-05-2019"));
        sensorDto.setValue(-5);
        sensorsDto.add(3, sensorDto);

        sensorDto = new SensorDto();
        sensorDto.setId(5L);
        sensorDto.setSensor("Test sensor 3");
        sensorDto.setObject("Test object 1");
        sensorDto.setDate(formatter.parse("31-05-2019"));
        sensorDto.setValue(22);
        sensorsDto.add(4, sensorDto);

        Sensor sensor = modelMapper.map(sensorsDto.get(0), Sensor.class);
        Mockito.when(sensorRepo.save(sensor))
                .thenReturn(sensor);
    }

    @Test
    public void save() {
        Sensor sensor = modelMapper.map(sensorsDto.get(0), Sensor.class);
        Mockito.when(sensorRepo.save(sensor))
                .thenReturn(sensor);

        sensorService.save(sensorsDto.get(0));
    }

    @Test
    public void getBySensorBetweenDates() throws Exception {
        Date startDate = formatter.parse("26-05-2019");
        Date endDate = formatter.parse("31-05-2019");

        Mockito.when(sensorRepo.findBySensorBetweenDates("Test sensor 1", startDate, endDate))
                .thenReturn(new ArrayList<Sensor>(){{
                        add(modelMapper.map(sensorsDto.get(0), Sensor.class));
                        add(modelMapper.map(sensorsDto.get(4), Sensor.class));
                }});

        List<SensorDto> sensorsDtoFound = sensorService.getBySensorBetweenDates(
                "Test sensor 1",
                "26-05-2019",
                "31-05-2019");

        assertThat(sensorsDtoFound).hasSize(2);
        assertThat(sensorsDtoFound.get(0).getId()).isEqualTo(sensorsDto.get(0).getId());
        assertThat(sensorsDtoFound.get(1).getId()).isEqualTo(sensorsDto.get(4).getId());
    }

    @Test
    public void getByObject() {
        List<Sensor> sensors = sensorsDto.stream().map(sensorDto ->
                modelMapper.map(sensorDto, Sensor.class)).collect(Collectors.toList());
        Mockito.when(sensorRepo.findByObject(sensors.get(0).getObject()))
                .thenReturn(sensors.stream()
                                .filter(sensor -> sensor.getObject().equals(sensors.get(0).getObject()))
                                .collect(Collectors.toList()));

        Map<String, Integer> found = sensorService.getByObject(sensorsDto.get(0).getObject());

        assertThat(found)
                .hasSize(2)
                .containsKeys(sensorsDto.get(0).getSensor());

        assertThat(found.get(sensorsDto.get(3).getSensor()))
                .isEqualTo(sensorsDto.get(4).getValue());

    }

    @Test
    public void getAll() {
        List<Sensor> sensors = sensorsDto.stream().map(sensorDto ->
                modelMapper.map(sensorDto, Sensor.class)).collect(Collectors.toList());
        Mockito.when(sensorRepo.findAll()).thenReturn(sensors);

        Map<String, Map<String, Integer>> found = sensorService.getAll();

        assertThat(found).hasSize(3);
        assertThat(found.get(sensorsDto.get(0).getObject())).hasSize(2);
        assertThat(found.get(sensorsDto.get(0).getObject())
                .get(sensorsDto.get(4).getSensor()))
                .isEqualTo(8);
    }
}