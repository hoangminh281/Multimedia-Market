package com.thm.hoangminh.multimediamarket.models;

public class Capacity {
    private int size;
    private String unit;

    public Capacity() {
    }

    public Capacity(int size, String unit) {
        this.size = size;
        this.unit = unit;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
