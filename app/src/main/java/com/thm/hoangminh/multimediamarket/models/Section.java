package com.thm.hoangminh.multimediamarket.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Section {
    private String section_id;
    private String title;
    private HashMap<String, String> game_id;

    public Section() {
    }


    public Section(String title, HashMap<String, String> game_id) {
        this.title = title;
        this.game_id = game_id;
    }

    public Section(String section_id, String title, HashMap<String, String> game_id) {
        this.section_id = section_id;
        this.title = title;
        this.game_id = game_id;
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

    public HashMap<String, String> getGame_id() {
        return game_id;
    }

    public void setGame_id(HashMap<String, String> game_id) {
        this.game_id = game_id;
    }

    public static List<Section> initializeData(){
        List<Section> mdata = new ArrayList<>();
        HashMap<String, String> data = new HashMap<>();
        mdata.add(new Section("Game mới + c.nhật", data));
        mdata.add(new Section("Thống trị Arcade", data));
        mdata.add(new Section("Được gợi ý cho bạn", data));
        mdata.add(new Section("Lập chiến lược và chinh phục", data));
        mdata.add(new Section("Đề xuất cho bạn", data));
        return mdata;
    }

    public static List<String> getGameId_ListString(HashMap<String, String> game_id) {
        return new ArrayList<>(game_id.values());
    }

}
