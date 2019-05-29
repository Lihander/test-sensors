package com.skoltech.testsensors.repo;

import com.skoltech.testsensors.domain.Sensor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SensorRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SensorRepo sensorRepo;

    private List<Sensor> sensors;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

    @Before
    public void setUp() throws Exception {
        sensors = new ArrayList<>();
        Sensor sensor = new Sensor();
        sensor.setSensor("Test sensor 1");
        sensor.setObject("Test object 1");
        sensor.setDate(formatter.parse("28-05-2019"));
        sensor.setValue(24);
        sensors.add(0, sensor);
        entityManager.persist(sensor);

        sensor = new Sensor();
        sensor.setSensor("Test sensor 2");
        sensor.setObject("Test object 2");
        sensor.setDate(formatter.parse("27-05-2019"));
        sensor.setValue(30);
        sensors.add(1, sensor);
        entityManager.persist(sensor);

        sensor = new Sensor();
        sensor.setSensor("Test sensor 1");
        sensor.setObject("Test object 3");
        sensor.setDate(formatter.parse("11-05-2019"));
        sensor.setValue(11);
        sensors.add(2, sensor);
        entityManager.persist(sensor);

        sensor = new Sensor();
        sensor.setSensor("Test sensor 3");
        sensor.setObject("Test object 1");
        sensor.setDate(formatter.parse("31-05-2019"));
        sensor.setValue(-5);
        sensors.add(3, sensor);
        entityManager.persist(sensor);
        entityManager.flush();
    }

    @Test
    public void findBySensorBetweenDatesTest() throws Exception {
        Sensor sensor = sensors.get(0);
        List<Sensor> foundSensors = sensorRepo.findBySensorBetweenDates(
                sensor.getSensor(),
                formatter.parse("27-05-2019"),
                formatter.parse("30-05-2019"));

        assertThat(foundSensors)
                .hasSize(1)
                .contains(sensors.get(0))
                .doesNotContain(sensors.get(1), sensors.get(2), sensors.get(3));
    }

    @Test
    public void findByObjectTest() {
        List<Sensor> foundSensors = sensorRepo.findByObject("Test object 1");

        assertThat(foundSensors)
                .hasSize(2)
                .contains(sensors.get(0), sensors.get(3))
                .doesNotContain(sensors.get(1), sensors.get(2));
    }
}