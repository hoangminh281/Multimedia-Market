package com.thm.hoangminh.multimediamarket.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String cate_id;
    private String name;

    public Category(){}

    public Category(String cate_id, String name) {
        this.cate_id = cate_id;
        this.name = name;
    }

    public String getCate_id() {
        return cate_id;
    }

    public void setCate_id(String cate_id) {
        this.cate_id = cate_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Category> initializeData() {
        List<Category> data = new ArrayList<>();
        data.add(new Category("-LDlhVwj9j3tU89adLiE", "Trò chơi"));
        data.add(new Category("-LDlhVwkEMtWv1ttsEVs", "Hình ảnh"));
        data.add(new Category("-LDlhVwlraRt24dXSmyx", "Video"));
        data.add(new Category("-LDlhVwmIEuMkLPNA9EK", "Âm thanh"));
        return data;
    }
}
