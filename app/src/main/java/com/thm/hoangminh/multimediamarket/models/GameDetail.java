package com.thm.hoangminh.multimediamarket.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameDetail {
    private String id;
    private String intro;
    private String description;
    private Capacity capacity;
    private int downloaded;
    private int ageLimit;
    private String owner_id;
    private String video;
    private HashMap<String, String> imageList;

    public GameDetail() {
    }

    public GameDetail(String intro, String description, Capacity capacity, int downloaded, int ageLimit, String owner_id, String video) {
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.owner_id = owner_id;
        this.video = video;
    }

    public GameDetail(String id, String intro, String description, Capacity capacity, int downloaded, int ageLimit, String owner_id, String video) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.owner_id = owner_id;
        this.video = video;
    }

    public GameDetail(String id, String intro, String description, Capacity capacity, int downloaded, int ageLimit, String owner_id, String video, HashMap<String, String> imageList) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.owner_id = owner_id;
        this.video = video;
        this.imageList = imageList;
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

    public Capacity getCapacity() {
        return capacity;
    }

    public void setCapacity(Capacity capacity) {
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

    public String getOwner_id() {
        return owner_id;

    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public HashMap<String, String> getImageList() {
        return imageList;
    }

    public void setImageList(HashMap<String, String> imageList) {
        this.imageList = imageList;
    }

    public static ArrayList<GameDetail> initializeData() {
        ArrayList<GameDetail> list = new ArrayList<>();
        list.add(new GameDetail("-LDlhVwX9fzrFxtdewJo", "Chơi các trò chơi trúng thưởng hơn 6 triệu người chơi!", "Now you can experience the uncompromising wilderness survuval game full of science and magic on the go.", new Capacity(110, "Mb"), 100000, 7, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "FBhVgaf9JS4"));
        list.add(new GameDetail("-LDlhVwesbhJsBsFVmEt", "Tốt nhất trò chơi hành động Bird!", "Use the slingshot to fling birds at the piggies' towers and bring them crashing down - all to save the precious eggs.", new Capacity(235, "Mb"), 100000000, 3, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "M0niPfYZaaI"));
        list.add(new GameDetail("-LDlhVwfP9wYLVhHgqI6", "Hyper-gây nghiện miễn phí phù hợp với câu đố phiêu lưu, cho mùa đông mới và 2018 năm mới", "What you can do in Ice Crush.", new Capacity(235, "Mb"), 1000000, 3, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "5hutuC91cIY"));
        list.add(new GameDetail("-LDlhVwgwHDj__CE6gIz", "Riêng công viên giải trí của bạn! Fun mini trò chơi, Rides và Giải thưởng", "*** Pay once & Play forever + receive FREE updates! No ads and no IAP ***", new Capacity(532, "Mb"), 5000, 3, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "1GpjE2Uogws"));
        list.add(new GameDetail("-LDlhVwh2F9hNBas5vHD", "Stickman Legends: Shadow Wars - Trò chơi hay nhất trong loạt game Stickman!", "Trong cuộc chiến chống lại các thế lực ác quỷ hắc ám (Shadow Wars), các chiến binh Stickman Warriors tham gia vào hành trình chinh phục thế giới hắc ám.", new Capacity(111, "Mb"), 1000000, 12, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "ILd4dVcXQnU"));
        list.add(new GameDetail("-LDlhVwj9j3tU89adLiE", "Vũ trụ đầy Mê Planets đang chờ bạn khám phá", "Maze Planet 3D Pro 2018.", new Capacity(74, "Mb"), 100000, 3, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "2CNTWH0zp9s"));
        list.add(new GameDetail("-LDlhVwkEMtWv1ttsEVs", "Cổ điển trò chơi phong cách JRPG trên điện thoại di động! Những cuộc phiêu lưu vĩ đại đang chờ bạn!", "This paid version of Mystic Gardian is full-screen ad free, and unlocking the final chapter is free of charge.", new Capacity(423, "Mb"), 100000, 7, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "hIw3TH2EOZ0"));
        list.add(new GameDetail("-LDlhVwlraRt24dXSmyx", "Shadow of Death: Stickman Fighting - Unlock tất cả Heroes. Thách thức kẻ thù của bạn!", "Shadow of Death is the greatest combination of Role-playing game (RPG) and Classic Fighting game.", new Capacity(344, "Mb"), 100000, 7, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "hUaBEZuYj_s"));
        list.add(new GameDetail("-LDlhVwmIEuMkLPNA9EK", "Nhận Souls và Gems trị giá $ 100 !! (Bạn cũng có thể lấy nó với gốc ID Ranking)", "The Brand New Idle RPG!!", new Capacity(33, "Mb"), 1000, 3, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "ghsWXmd75SE"));
        list.add(new GameDetail("-LDlhVwnkVIDaZrdnmhy", "Một cuộc phiêu lưu huyền ảo của kiến trúc không thể và sự tha thứ", "In Monument Valley you will manipulate impossible architecture and guide a silent princess through a stunningly beautifull world", new Capacity(421, "Mb"), 1000000, 3, "MVu7NI0XgNQxvxuIqEa19xJXdin1", "wC1jHHF_Wjo"));
        return list;
    }

}
