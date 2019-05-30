package com.skoltech.testsensors.service.impl;

import com.skoltech.testsensors.domain.Building;
import com.skoltech.testsensors.domain.Sensor;
import com.skoltech.testsensors.repo.BuildingRepo;
import com.skoltech.testsensors.service.BuildingService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingServiceImpl implements BuildingService {
    private final BuildingRepo buildingRepo;

    @Autowired
    public BuildingServiceImpl(BuildingRepo buildingRepo) {
        this.buildingRepo = buildingRepo;
    }

    @Override
    public Map<Long, Integer> getCurrentValueByBuildingId(Long id) {
        Building building = buildingRepo.getOne(id);
        List<Sensor> sensors = building.getSensors();
        return sensors.stream()
                .collect(Collectors.toMap(Sensor::getId, Sensor::getCurrentValue));
    }
}
