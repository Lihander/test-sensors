package com.skoltech.testsensors.domain.dto;

import java.util.List;

public class SensorDto extends AbstractDto {
    private Integer currentValue;
    private Long buildingId;
    private List<SensorHistoryDto> sensorHistory;

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public List<SensorHistoryDto> getSensorHistory() {
        return sensorHistory;
    }

    public void setSensorHistory(List<SensorHistoryDto> sensorHistory) {
        this.sensorHistory = sensorHistory;
    }
}
