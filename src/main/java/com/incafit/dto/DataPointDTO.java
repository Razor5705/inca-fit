package com.incafit.dto;

public class DataPointDTO {
    private String label;
    private Number value;

    public DataPointDTO(String label, Number value) {
        this.label = label;
        this.value = value;
    }

    // Getters and Setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }
}
