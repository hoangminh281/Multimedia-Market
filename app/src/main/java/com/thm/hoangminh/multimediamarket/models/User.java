package com.thm.hoangminh.multimediamarket.models;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dell on 5/7/2018.
 */

public class User {
    private String id, name, image, email, phone, birthday, sex;
    private double balance;
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    public User() {}

    public User(String name, String image, String email, String phone, String birthday, String sex) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
    }

    public User(String id, String name, String image, String email, String phone, String birthday, String sex) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
    }

    public User(String id, String name, String image, String email, String phone, String birthday, String sex, double balance) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.balance = balance;
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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void createUserOnFirebase() {
        Log.d("User", "createUserOnFirebase..." + name);
        mRef.child("users/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mRef.child("users/" + id).setValue(User.this);
                    Log.d("User", "createUserOnFirebase success");
                }
                Log.d("User", "createUserOnFirebase exist" + name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", "createUserOnFirebase failure" + databaseError);
            }
        });

    }

}
