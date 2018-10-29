package com.thm.hoangminh.multimediamarket.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by Dell on 5/7/2018.
 */

public class User {
    private String id, name, image, email, phone, birthday;
    private int sex, role, status;
    private double balance;

    private final String DEFAULT_IMG_KEY = "user.png";

    private static User instance;

    public static User getInstance() {
        if (instance == null) instance = new User();
        return instance;
    }

    public static User setInstance(User user) {
        instance = new User(user);
        return instance;
    }

    public User() {
    }

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.image = user.image;
        this.email = user.email;
        this.phone = user.phone;
        this.birthday = user.birthday;
        this.sex = user.sex;
        this.balance = user.balance;
        this.role = user.role;
        this.status = user.status;
    }

    public User(String name, String image, String email, String phone, String birthday, int sex, int role, int balance, int status) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.balance = balance;
        this.role = role;
        this.status = status;
    }

    public User(String id, String name, String image, String email, String phone, String birthday, int sex, double balance, int role, int status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.balance = balance;
        this.role = role;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
