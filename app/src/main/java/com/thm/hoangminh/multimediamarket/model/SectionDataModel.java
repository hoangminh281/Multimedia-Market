package com.thm.hoangminh.multimediamarket.model;

import java.util.ArrayList;
import java.util.List;

public class SectionDataModel {
    private String cateId;
    private String sectionId;
    private String headerTitle;
    private ArrayList<Product> allItemsInSection;
    private List<String> productIdArr;
    private boolean requestLock;

    public SectionDataModel() {
        this.allItemsInSection = new ArrayList<>();
    }

    public SectionDataModel(String headerTitle, ArrayList<Product> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public SectionDataModel(String sectionId, String headerTitle, ArrayList<Product> allItemsInSection) {
        this.sectionId = sectionId;
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<Product> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<Product> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public void addItemInSection(Product product) {
        this.allItemsInSection.add(product);
    }

    public List<String> getProductIdArr() {
        return productIdArr;
    }

    public void setProductIdArr(List<String> productIdArr) {
        this.productIdArr = productIdArr;
    }

    public boolean isRequestLock() {
        return requestLock;
    }

    public void setRequestLock(boolean requestLock) {
        this.requestLock = requestLock;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }
}
