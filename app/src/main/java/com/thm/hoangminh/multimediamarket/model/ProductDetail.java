package com.thm.hoangminh.multimediamarket.model;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductDetail {
    private String id;
    private String intro;
    private String description;
    private int capacity;
    private int downloaded;
    private int ageLimit;
    private String ownerId;
    private String videoId;
    private HashMap<String, String> imageIdList;
    private String fileId;

    public ProductDetail() {
    }

    public ProductDetail(String intro, String description, int capacity, int downloaded, int ageLimit, String ownerId, String videoId, String fileId) {
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.ownerId = ownerId;
        this.videoId = videoId;
        this.fileId = fileId;
    }

    public ProductDetail(String id, String intro, String description, int capacity, int downloaded, int ageLimit, String ownerId, String videoId, String fileId) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.ownerId = ownerId;
        this.videoId = videoId;
        this.fileId = fileId;
    }

    public ProductDetail(String id, String intro, String description, int capacity, int downloaded, int ageLimit, String ownerId, String videoId, HashMap<String, String> imageIdList, String fileId) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.ownerId = ownerId;
        this.videoId = videoId;
        this.imageIdList = imageIdList;
        this.fileId = fileId;
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

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
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
}
