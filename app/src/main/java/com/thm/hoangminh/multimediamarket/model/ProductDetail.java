package com.thm.hoangminh.multimediamarket.model;

import java.util.HashMap;

public class ProductDetail {
    private String id;
    private String intro;
    private String description;
    private int capacity;
    private int buyCount;
    private int ageLimit;
    private HashMap<String, Integer> views;
    private String ownerId;
    private String videoId;
    private HashMap<String, String> imageIdList;
    private String fileId;

    public ProductDetail() {
    }

    public ProductDetail(String id, String intro, String description, int capacity, int buyCount, int ageLimit, String ownerId, String videoId, HashMap<String, String> imageIdList, String fileId, HashMap<String, Integer> views) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.buyCount = buyCount;
        this.ageLimit = ageLimit;
        this.ownerId = ownerId;
        this.videoId = videoId;
        this.imageIdList = imageIdList;
        this.fileId = fileId;
        this.views = views;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getVideoId() {
        return videoId == null ? "" : videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public HashMap<String, String> getImageIdList() {
        return imageIdList;
    }

    public void setImageIdList(HashMap<String, String> imageIdList) {
        this.imageIdList = imageIdList;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public HashMap<String, Integer> getViews() {
        return views;
    }

    public void setViews(HashMap<String, Integer> views) {
        this.views = views;
    }
}
