package com.skoltech.testsensors.domain;

import com.skoltech.testsensors.domain.dto.AbstractDto;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Sensor extends AbstractEntity {
    @Column(nullable = false)
    private Integer currentValue;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SensorHistory> sensorHistory;

    public Sensor() {
    }

    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer currentValue) {
        this.currentValue = currentValue;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public List<SensorHistory> getSensorHistory() {
        return sensorHistory;
    }

    public void setSensorHistory(List<SensorHistory> sensorHistory) {
        this.sensorHistory = sensorHistory;
    }
}
