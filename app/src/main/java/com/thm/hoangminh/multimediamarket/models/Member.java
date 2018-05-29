package com.thm.hoangminh.multimediamarket.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Dell on 5/7/2018.
 */

public class Member {
    String id, name, image, email, phone, birthday, sex;

    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    public Member(String name, String image, String email, String phone, String birthday, String sex) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
    }

    public Member(String id, String name, String image, String email, String phone, String birthday, String sex) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
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

    public void createMemberOnFirebase() {
        Log.d("Member", "createMemberOnFirebase..." + name);
        mRef.child("members/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    mRef.child("members/" + id).setValue(Member.this);
                    Log.d("Member", "createMemberOnFirebase success");
                }
                Log.d("Member", "createMemberOnFirebase exist" + name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Member", "createMemberOnFirebase failure" + databaseError);
            }
        });

    }

}
