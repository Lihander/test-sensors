package com.skoltech.testsensors.domain.dto;

import java.util.Date;

public class SensorHistoryDto extends AbstractDto {
    private Integer value;
    private Date date;
    private Long sensorId;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }
}
