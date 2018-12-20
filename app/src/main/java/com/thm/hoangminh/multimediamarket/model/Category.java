package com.thm.hoangminh.multimediamarket.model;

import java.util.ArrayList;

public class Category {
    private String cateId;
    private String name;

    private static ArrayList<Category> instance;

    public static ArrayList<Category> getInstance() {
        if (instance == null) instance = new ArrayList<Category>();
        return instance;
    }

    public static ArrayList<Category> setInstance(ArrayList<Category> category) {
        instance = category;
        return instance;
    }

    public Category(){}

    public Category(String cateId, String name) {
        this.cateId = cateId;
        this.name = name;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
