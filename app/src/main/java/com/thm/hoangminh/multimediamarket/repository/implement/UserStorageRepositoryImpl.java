package com.thm.hoangminh.multimediamarket.repository.implement;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;
import com.thm.hoangminh.multimediamarket.repository.ImageRepository;

import java.util.ArrayList;

public class UserImageRepositoryImpl implements ImageRepository {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    public void add(Bitmap item) {

    }

    @Override
    public void update(Bitmap item) {

    }

    @Override
    public void remove(Bitmap item) {

    }

    @Override
    public ArrayList<Bitmap> findAll(OnSuccessListener<Uri> event) {
        return null;
    }

    @Override
    public void findById(String userImageId, OnSuccessListener<Uri> event) {
        mStorageRef.child(ROUTE.USER_IMAGE(userImageId)).getDownloadUrl().addOnSuccessListener(event);
    }
}
