package com.skoltech.testsensors.domain.dto;

public abstract class AbstractDto {
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
