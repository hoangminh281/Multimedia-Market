package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.repository.base.WatchingRepository;

public interface UserRepository extends WatchingRepository<User, String> {
    void findBalance(String userId, ValueEventListener event);

    ValueEventListener findAndWatchRoleByUserId(String userId, ValueEventListener event);

    void destroyFindAndWatchRoleByUserId(String userId, ValueEventListener event);

    void setBalance(String userId, double balance, OnSuccessListener successListener, OnFailureListener failureListener);

    void setImagePath(String userId, String imagePath, OnSuccessListener successListener, OnFailureListener failureListener);

    void setUsername(String userId, String userName, OnSuccessListener successListener, OnFailureListener failureListener);

    void setEmail(String userId, String email, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    void setBirthday(String userId, String birthday, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    void setGender(String userId, int genderId, OnSuccessListener<Void> successListener, OnFailureListener failureListener);

    void findImageId(String userId, ValueEventListener eventListener);
}
