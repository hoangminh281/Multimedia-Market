package com.thm.hoangminh.multimediamarket.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductDetail {
    private String id;
    private String intro;
    private String description;
    private int capacity;
    private int downloaded;
    private int ageLimit;
    private String owner_id;
    private String video;
    private HashMap<String, String> imageList;
    private String downloadLink;

    public ProductDetail() {
    }

    public ProductDetail(String intro, String description, int capacity, int downloaded, int ageLimit, String owner_id, String video, String downloadLink) {
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.owner_id = owner_id;
        this.video = video;
        this.downloadLink = downloadLink;
    }

    public ProductDetail(String id, String intro, String description, int capacity, int downloaded, int ageLimit, String owner_id, String video, String downloadLink) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.owner_id = owner_id;
        this.video = video;
        this.downloadLink = downloadLink;
    }

    public ProductDetail(String id, String intro, String description, int capacity, int downloaded, int ageLimit, String owner_id, String video, HashMap<String, String> imageList, String downloadLink) {
        this.id = id;
        this.intro = intro;
        this.description = description;
        this.capacity = capacity;
        this.downloaded = downloaded;
        this.ageLimit = ageLimit;
        this.owner_id = owner_id;
        this.video = video;
        this.imageList = imageList;
        this.downloadLink = downloadLink;
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

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public static ArrayList<ProductDetail> initializeGame() {
        ArrayList<ProductDetail> list = new ArrayList<>();
        list.add(new ProductDetail("-LDlhVwX9fzrFxtdewJo", "Chơi các trò chơi trúng thưởng hơn 6 triệu người chơi!", "Now you can experience the uncompromising wilderness survuval product full of science and magic on the go.", 110, 100000, 7, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "FBhVgaf9JS4",""));
        list.add(new ProductDetail("-LDlhVwesbhJsBsFVmEt", "Tốt nhất trò chơi hành động Bird!", "Use the slingshot to fling birds at the piggies' towers and bring them crashing down - all to save the precious eggs.", 235, 100000000, 3, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "M0niPfYZaaI",""));
        list.add(new ProductDetail("-LDlhVwfP9wYLVhHgqI6", "Hyper-gây nghiện miễn phí phù hợp với câu đố phiêu lưu, cho mùa đông mới và 2018 năm mới", "What you can do in Ice Crush.", 235, 1000000, 3, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "5hutuC91cIY",""));
        list.add(new ProductDetail("-LDlhVwgwHDj__CE6gIz", "Riêng công viên giải trí của bạn! Fun mini trò chơi, Rides và Giải thưởng", "*** Pay once & Play forever + receive FREE updates! No ads and no IAP ***", 532, 5000, 3, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "1GpjE2Uogws",""));
        list.add(new ProductDetail("-LDlhVwh2F9hNBas5vHD", "Stickman Legends: Shadow Wars - Trò chơi hay nhất trong loạt product Stickman!", "Trong cuộc chiến chống lại các thế lực ác quỷ hắc ám (Shadow Wars), các chiến binh Stickman Warriors tham gia vào hành trình chinh phục thế giới hắc ám.", 111, 1000000, 12, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "ILd4dVcXQnU",""));
        list.add(new ProductDetail("-LDlhVwj9j3tU89adLiE", "Vũ trụ đầy Mê Planets đang chờ bạn khám phá", "Maze Planet 3D Pro 2018.", 74, 100000, 3, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "2CNTWH0zp9s",""));
        list.add(new ProductDetail("-LDlhVwkEMtWv1ttsEVs", "Cổ điển trò chơi phong cách JRPG trên điện thoại di động! Những cuộc phiêu lưu vĩ đại đang chờ bạn!", "This paid version of Mystic Gardian is full-screen ad free, and unlocking the final chapter is free of charge.", 423, 100000, 7, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "hIw3TH2EOZ0",""));
        list.add(new ProductDetail("-LDlhVwlraRt24dXSmyx", "Shadow of Death: Stickman Fighting - Unlock tất cả Heroes. Thách thức kẻ thù của bạn!", "Shadow of Death is the greatest combination of Role-playing product (RPG) and Classic Fighting product.", 344, 100000, 7, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "hUaBEZuYj_s",""));
        list.add(new ProductDetail("-LDlhVwmIEuMkLPNA9EK", "Nhận Souls và Gems trị giá $ 100 !! (Bạn cũng có thể lấy nó với gốc ID Ranking)", "The Brand New Idle RPG!!", 33, 1000, 3, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "ghsWXmd75SE",""));
        list.add(new ProductDetail("-LDlhVwnkVIDaZrdnmhy", "Một cuộc phiêu lưu huyền ảo của kiến trúc không thể và sự tha thứ", "In Monument Valley you will manipulate impossible architecture and guide a silent princess through a stunningly beautifull world", 421, 1000000, 3, "8RQoEWD3s7bMfCCWFleuvnB7aw23", "wC1jHHF_Wjo",""));
        return list;
    }

    public void setBitmapImage(final ImageView img, String image_id,final Context context) {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("products/" + image_id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context)
                            .load(uri)
                            .error(R.mipmap.icon_app_2)
                            .into(img);
                }
            });
        }
}
