package com.thm.hoangminh.multimediamarket.model;

import java.util.HashMap;

public class Section {
    private String sectionId;
    private String title;
    private HashMap<String, Integer> productIdArr;

    public Section() {
    }

    public Section(String title, HashMap<String, Integer> productIdArr) {
        this.title = title;
        this.productIdArr = productIdArr;
    }

    public Section(String sectionId, String title) {
        this.sectionId = sectionId;
        this.title = title;
    }

    public Section(String sectionId, String title, HashMap<String, Integer> productIdArr) {
        this.sectionId = sectionId;
        this.title = title;
        this.productIdArr = productIdArr;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Integer> getProductIdArr() {
        return productIdArr;
    }

    public void setProductIdArr(HashMap<String, Integer> productIdArr) {
        this.productIdArr = productIdArr;
    }
}
