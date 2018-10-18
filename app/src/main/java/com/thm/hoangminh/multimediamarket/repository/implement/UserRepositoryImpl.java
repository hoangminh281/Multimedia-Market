package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;

public class UserRepositoryImpl implements UserRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(User user, ValueEventListener valueEventListener) {
        mRef.child(ROUTE.USER(user.getId())).setValue(user);
    }

    @Override
    public void update(User user, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(User user, ValueEventListener valueEventListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {

    }

    @Override
    public void findById(String id, ValueEventListener event) {
        mRef.child(ROUTE.USER(id)).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findAndWatchById(String id, ValueEventListener event) {
        mRef.child(ROUTE.USER(id)).addValueEventListener(event);
    }

    @Override
    public void findAndWatch(ValueEventListener event) {

    }

    @Override
    public void findBalance(String userId, ValueEventListener event) {
        mRef.child(ROUTE.USER_BALANCE(userId)).addListenerForSingleValueEvent(event);
    }

    @Override
    public ValueEventListener findAndWatchRoleByUserId(String userId, ValueEventListener event) {
        return mRef.child(ROUTE.USER_ROLE(userId)).addValueEventListener(event);
    }

    @Override
    public void destroyFindAndWatchRoleByUserId(String userId, ValueEventListener event) {
        mRef.child(ROUTE.USER_ROLE(userId)).removeEventListener(event);
    }

    @Override
    public void setBalance(String userId, double balance, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.USER_BALANCE(userId)).setValue(balance)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setImagePath(String userId, String imagePath, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.USER_IMAGE(userId)).setValue(imagePath)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setUsername(String userId, String userName, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.USER_NAME(userId)).setValue(userName)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setEmail(String userId, String email, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.USER_EMAIL(userId)).setValue(email)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setBirthday(String userId, String birthday, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.USER_BIRTHDAY(userId)).setValue(birthday)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setGender(String userId, int genderId, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.USER_GENDER(userId)).setValue(genderId)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
