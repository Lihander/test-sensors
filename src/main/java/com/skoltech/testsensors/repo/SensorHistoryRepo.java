package com.skoltech.testsensors.repo;

import com.skoltech.testsensors.domain.SensorHistory;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHistoryRepo extends JpaRepository<SensorHistory, Long> {
    @Query(value = "from SensorHistory s where sensor_id = :id and date BETWEEN :startDate AND :endDate")
    List<SensorHistory> findBySensorIdBetweenDates(@Param("id") Long id, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
}




