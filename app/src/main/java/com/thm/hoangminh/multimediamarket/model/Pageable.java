package com.thm.hoangminh.multimediamarket.model;

public class Pageable {
    private int limit;

    private String firstId;

    public Pageable(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }
}
