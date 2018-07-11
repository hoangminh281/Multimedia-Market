package com.thm.hoangminh.multimediamarket.models;

import java.util.ArrayList;
import java.util.List;

public class SectionDataModel {
    private String cate_id;
    private String section_id;
    private String headerTitle;
    private ArrayList<Product> allItemsInSection;
    private List<String> product_id_arr;
    private boolean request_deny;

    public SectionDataModel() {
        this.allItemsInSection = new ArrayList<>();
    }

    public SectionDataModel(String headerTitle, ArrayList<Product> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public SectionDataModel(String section_id, String headerTitle, ArrayList<Product> allItemsInSection) {
        this.section_id = section_id;
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
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

    public List<String> getProduct_id_arr() {
        return product_id_arr;
    }

    public void setProduct_id_arr(List<String> product_id_arr) {
        this.product_id_arr = product_id_arr;
    }

    public boolean isRequest_deny() {
        return request_deny;
    }

    public void setRequest_deny(boolean request_deny) {
        this.request_deny = request_deny;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }
}
