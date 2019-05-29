package com.skoltech.testsensors.repo;

import com.skoltech.testsensors.domain.Sensor;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepo extends JpaRepository<Sensor, Long> {
    @Query(value = "from Sensor s where sensor = :sensor and date BETWEEN :startDate AND :endDate")
    List<Sensor> findBySensorBetweenDates(@Param("sensor") String sensor, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Sensor> findByObject(String object);
}
