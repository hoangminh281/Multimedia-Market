package com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public class GameDetailInteractor {

    private GameDetailListener listener;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;

    public GameDetailInteractor(GameDetailListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void LoadGameDetailById(String game_id) {
        mRef.child("game_detail/" + game_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadGameDetailByIdSuccess(dataSnapshot.getValue(GameDetail.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadOwnerById(String owner_id) {
        mRef.child("members/" + owner_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadOwnerSuccess(dataSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getImageLinkDownload(String game_id, ArrayList<String> imageList) {
        for (String img : imageList) {
            mStorageRef.child("game_details/" + game_id + "/" + img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    listener.onLoadImageLinkSuccess(uri.toString());
                }
            });
        }
    }
}
