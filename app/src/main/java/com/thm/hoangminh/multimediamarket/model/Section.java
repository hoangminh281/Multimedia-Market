package com.thm.hoangminh.multimediamarket.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Section {
    private String section_id;
    private String title;
    private HashMap<String, Integer> product_id;

    public Section() {
    }


    public Section(String title, HashMap<String, Integer> product_id) {
        this.title = title;
        this.product_id = product_id;
    }

    public Section(String section_id, String title) {
        this.section_id = section_id;
        this.title = title;
    }

    public Section(String section_id, String title, HashMap<String, Integer> product_id) {
        this.section_id = section_id;
        this.title = title;
        this.product_id = product_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Integer> getProduct_id() {
        return product_id;
    }

    public void setProduct_id(HashMap<String, Integer> product_id) {
        this.product_id = product_id;
    }

    public static List<Section> initializeHomeSection() {
        List<Section> mdata = new ArrayList<>();
        HashMap<String, Integer> data = new HashMap<>();
        data.put("-LDlhVwX9fzrFxtdewJo", 1);
        data.put("-LDlhVwesbhJsBsFVmEt", 1);
        data.put("-LDlhVwfP9wYLVhHgqI6", 1);
        data.put("-LDlhVwgwHDj__CE6gIz", 1);
        data.put("-LDlhVwh2F9hNBas5vHD", 1);
        data.put("-LDlhVwj9j3tU89adLiE", 1);
        data.put("-LDlhVwkEMtWv1ttsEVs", 1);
        mdata.add(new Section("Game mới + c.nhật", data));
        mdata.add(new Section("Thống trị Arcade", data));
        mdata.add(new Section("Được gợi ý cho bạn", data));
        mdata.add(new Section("Lập chiến lược và chinh phục", data));
        mdata.add(new Section("Đề xuất cho bạn", data));
        mdata.add(new Section("Game nhập vai", data));
        mdata.add(new Section("Game sinh tồn", data));
        mdata.add(new Section("Game phiêu lưu", data));
        return mdata;
    }

    public static List<Section> initializeGameSection() {
        List<Section> mdata = new ArrayList<>();
        HashMap<String, Integer> data = new HashMap<>();
        data.put("-LDlhVwX9fzrFxtdewJo", 1);
        data.put("-LDlhVwesbhJsBsFVmEt", 1);
        data.put("-LDlhVwfP9wYLVhHgqI6", 1);
        data.put("-LDlhVwgwHDj__CE6gIz", 1);
        data.put("-LDlhVwh2F9hNBas5vHD", 1);
        data.put("-LDlhVwj9j3tU89adLiE", 1);
        data.put("-LDlhVwkEMtWv1ttsEVs", 1);
        mdata.add(new Section("Chiến thuật", data));
        mdata.add(new Section("Đua xe", data));
        mdata.add(new Section("Nhập vai", data));
        mdata.add(new Section("Phiêu lưu", data));
        mdata.add(new Section("Thể thao", data));
        mdata.add(new Section("Câu đố", data));
        mdata.add(new Section("Giáo dục", data));
        mdata.add(new Section("Mô phỏng", data));
        return mdata;
    }

    public static List<Section> initializeImageSection() {
        List<Section> mdata = new ArrayList<>();
        HashMap<String, Integer> data = new HashMap<>();
        data.put("-LGnvNA2YysMvdxCvwAa", 1);
        data.put("-LGoI58soDNPKJ9rivKv", 1);
        mdata.add(new Section("Animal", data));
        mdata.add(new Section("Nature", data));
        mdata.add(new Section("Autumn", data));
        mdata.add(new Section("Flower", data));
        mdata.add(new Section("Love", data));
        mdata.add(new Section("Texure", data));
        mdata.add(new Section("Wallpaper", data));
        mdata.add(new Section("Business", data));
        return mdata;
    }

    public static List<String> getProductId_ListString(HashMap<String, Integer> product_id) {
        return new ArrayList<>(product_id.keySet());
    }

}
