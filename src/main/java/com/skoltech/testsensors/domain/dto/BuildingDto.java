package com.skoltech.testsensors.domain.dto;

import java.util.List;

public class BuildingDto extends AbstractDto {
    private String address;
    private List<SensorDto> sensors;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<SensorDto> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorDto> sensors) {
        this.sensors = sensors;
    }
}
