package com.thm.hoangminh.multimediamarket.model;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String productId;
    private String cateId;
    private String title;
    private String photoId;
    private double rating;
    private double price;
    private String imageUri;
    private int status; //0: inactive, 1: active

    public Product() {
    }

    public Product(String productId, String cateId, String title, String photoId, double rating, double price, int status) {
        this.productId = productId;
        this.cateId = cateId;
        this.title = title;
        this.photoId = photoId;
        this.rating = rating;
        this.price = price;
        this.status = status;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static List<Product> initializeGame() {
        List<Product> data = new ArrayList<>();
        data.add(new Product("-LDlhVwX9fzrFxtdewJo", "-LGAQqsSdhMt2bTudm1L", "Don't starve: Pocket Edition", "dont-starve.png", 4.4, 110000, 1));
        data.add(new Product("-LDlhVwesbhJsBsFVmEt", "-LGAQqsSdhMt2bTudm1L", "Angry Bird 2", "angry-birds-2.png", 4.6, 0, 1));
        data.add(new Product("-LDlhVwfP9wYLVhHgqI6", "-LGAQqsSdhMt2bTudm1L", "Ice Crush 2018 - A new Puzzle Matching Adventure", "ice-crush-2018.png", 4.6, 0, 1));
        data.add(new Product("-LDlhVwgwHDj__CE6gIz", "-LGAQqsSdhMt2bTudm1L", "My Town : ICEE™ Amusement Park", "my-town.png", 4.7, 22000, 1));
        data.add(new Product("-LDlhVwh2F9hNBas5vHD", "-LGAQqsSdhMt2bTudm1L", "Stickman Legends", "stickman.png", 4.3, 6000, 1));
        data.add(new Product("-LDlhVwj9j3tU89adLiE", "-LGAQqsSdhMt2bTudm1L", "Maze Planet 3D Pro", "maze-planet.png", 4.5, 27000, 1));
        data.add(new Product("-LDlhVwkEMtWv1ttsEVs", "-LGAQqsSdhMt2bTudm1L", "Mystic Guardian VIP : Old School Action RPG", "mystic-guardian.png", 4.3, 80000, 1));
        data.add(new Product("-LDlhVwlraRt24dXSmyx", "-LGAQqsSdhMt2bTudm1L", "Shadow of Death: Stickman Fighting - Dark Knight", "shadow-of-death.png", 4.6, 9000, 1));
        data.add(new Product("-LDlhVwmIEuMkLPNA9EK", "-LGAQqsSdhMt2bTudm1L", "CashKnight ( Soul Event Version )", "cashknight.png", 4.5, 213000, 1));
        data.add(new Product("-LDlhVwnkVIDaZrdnmhy", "-LGAQqsSdhMt2bTudm1L", "Monument Valley", "monyment-valley.png", 4.8, 86000, 1));
        return data;
    }

    public void setBitmapImage(final ImageView img, final Context context) {
        if (this.getStatus() == 0) img.setColorFilter(R.color.white_transparent);
        else img.clearColorFilter();
        if (imageUri != null) {
            Picasso.with(context)
                    .load(imageUri)
                    .error(R.mipmap.icon_app_2)
                    .into(img);
        } else {
            final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
            mStorageRef.child("products/" + photoId).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imageUri = uri.toString();
                    Picasso.with(context)
                            .load(uri)
                            .error(R.mipmap.icon_app_2)
                            .into(img);
                }
            });
        }
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("products/" + this.getProductId() + "/status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getValue(int.class) == 0)
                        img.setColorFilter(R.color.white_transparent);
                    else img.clearColorFilter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
