package com.thm.hoangminh.multimediamarket.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private String game_id;
    private String title;
    private String photoId;
    private double rate;
    private Price price;

    public Game() {
    }

    public Game(String title, String photoId, double rate, Price price) {
        this.title = title;
        this.photoId = photoId;
        this.rate = rate;
        this.price = price;
    }

    public Game(String game_id, String title, String photoId, double rate, Price price) {
        this.game_id = game_id;
        this.title = title;
        this.photoId = photoId;
        this.rate = rate;
        this.price = price;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public static List<Game> initializeData(){
        List<Game> data = new ArrayList<>();
        data.add(new Game("Don't starve", "game.png", 8.6, new Price(95000, "đ")));
        data.add(new Game("Counter-Strike: Global Offensive", "game.png", 9.2, new Price(24300, "đ")));
        data.add(new Game("Tree of life", "game.png", 7.4, new Price(22666, "đ")));
        data.add(new Game("Need for speed", "game.png", 3.5, new Price(97500, "đ")));
        data.add(new Game("Clash of clan", "game.png", 4.4, new Price(86433, "đ")));
        data.add(new Game("Rule of survival", "game.png", 6.8, new Price(22000, "đ")));
        data.add(new Game("PLAYERUNKNOWN'S BATTLEGROUNDS", "game.png", 7.4, new Price(10900, "đ")));
        return data;
    }

    public void setBitmapImage(final ImageView img) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        try {
            final File localFile = File.createTempFile(photoId, ".png");
            mStorageRef.child("games/" + photoId).getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            final Bitmap[] bitmap = new Bitmap[1];
                            bitmap[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            img.setImageBitmap(bitmap[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e) {
        }
    }
}
