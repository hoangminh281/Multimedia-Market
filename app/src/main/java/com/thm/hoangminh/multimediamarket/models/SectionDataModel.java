package com.thm.hoangminh.multimediamarket.models;

import java.util.ArrayList;
import java.util.List;

public class SectionDataModel {

    private String section_id;
    private String headerTitle;
    private ArrayList<Game> allItemsInSection;
    private List<String> game_id_arr;
    private boolean request_deny;

    public SectionDataModel() {
        this.allItemsInSection = new ArrayList<>();
    }

    public SectionDataModel(String headerTitle, ArrayList<Game> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public SectionDataModel(String section_id, String headerTitle, ArrayList<Game> allItemsInSection) {
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

    public ArrayList<Game> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<Game> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    public void addItemInSection(Game game) {
        this.allItemsInSection.add(game);
    }

    public List<String> getGame_id_arr() {
        return game_id_arr;
    }

    public void setGame_id_arr(List<String> game_id_arr) {
        this.game_id_arr = game_id_arr;
    }

    public boolean isRequest_deny() {
        return request_deny;
    }

    public void setRequest_deny(boolean request_deny) {
        this.request_deny = request_deny;
    }
}
