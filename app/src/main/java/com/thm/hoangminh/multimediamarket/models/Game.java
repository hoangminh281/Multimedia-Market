package com.thm.hoangminh.multimediamarket.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Game implements Serializable {
    private String game_id;
    private String title;
    private String photoId;
    private double rating;
    private Price price;
    private String image_url;

    public Game() {
    }

    public Game(String title, String photoId, double rating, Price price) {
        this.title = title;
        this.photoId = photoId;
        this.rating = rating;
        this.price = price;
    }

    public Game(String game_id, String title, String photoId, double rating, Price price) {
        this.game_id = game_id;
        this.title = title;
        this.photoId = photoId;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rate) {
        this.rating = rate;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public static List<Game> initializeData() {
        List<Game> data = new ArrayList<>();
        data.add(new Game("-LDlhVwX9fzrFxtdewJo", "Don't starve: Pocket Edition", "dont-starve.png", 4.4, new Price(110000, "đ")));
        data.add(new Game("-LDlhVwesbhJsBsFVmEt", "Angry Bird 2", "angry-birds-2.png", 4.6, new Price(0, "đ")));
        data.add(new Game("-LDlhVwfP9wYLVhHgqI6", "Ice Crush 2018 - A new Puzzle Matching Adventure", "ice-crush-2018.png", 4.6, new Price(0, "đ")));
        data.add(new Game("-LDlhVwgwHDj__CE6gIz", "My Town : ICEE™ Amusement Park", "my-town.png", 4.7, new Price(22000, "đ")));
        data.add(new Game("-LDlhVwh2F9hNBas5vHD", "Stickman Legends", "stickman.png", 4.3, new Price(6000, "đ")));
        data.add(new Game("-LDlhVwj9j3tU89adLiE", "Maze Planet 3D Pro", "maze-planet.png", 4.5, new Price(27000, "đ")));
        data.add(new Game("-LDlhVwkEMtWv1ttsEVs", "Mystic Guardian VIP : Old School Action RPG", "mystic-guardian.png", 4.3, new Price(80000, "đ")));
        data.add(new Game("-LDlhVwlraRt24dXSmyx", "Shadow of Death: Stickman Fighting - Dark Knight", "shadow-of-death.png", 4.6, new Price(9000, "đ")));
        data.add(new Game("-LDlhVwmIEuMkLPNA9EK", "CashKnight ( Soul Event Version )", "cashknight.png", 4.5, new Price(213000, "đ")));
        data.add(new Game("-LDlhVwnkVIDaZrdnmhy", "Monument Valley", "monyment-valley.png", 4.8, new Price(86000, "đ")));
        return data;
    }

    public void setBitmapImage(final ImageView img, final Context context) {
        int image_size = (int) context.getResources().getDimension(R.dimen.image_size);
        if (image_url != null) {
            Picasso.with(context)
                    .load(image_url)
                    .resize(image_size, image_size)
                    .centerInside()
                    .into(img);
        } else {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("games/" + photoId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    int image_size = (int) context.getResources().getDimension(R.dimen.image_size);
                    image_url = uri.toString();
                    Picasso.with(context)
                            .load(uri)
                            .resize(image_size, image_size)
                            .centerInside()
                            .into(img);
                }
            });
        }
    }
}
