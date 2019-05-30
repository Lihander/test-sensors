package com.skoltech.testsensors.service;

import java.util.Map;

public interface BuildingService {
    Map<Long, Integer> getCurrentValueByBuildingId(Long id);
}
