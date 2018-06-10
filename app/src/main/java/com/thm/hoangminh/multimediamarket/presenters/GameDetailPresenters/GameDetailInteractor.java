package com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters;

import android.media.Rating;
import android.net.Uri;
import android.support.annotation.NonNull;

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
import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public class GameDetailInteractor {

    private GameDetailListener listener;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private FirebaseUser firebaseUser;

    public GameDetailInteractor(GameDetailListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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

    public void LoadRating(String game_id) {
        mRef.child("rating/" + game_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<RatingContent> ratingList = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        RatingContent ratingContent = item.getValue(RatingContent.class);
                        ratingContent.setUser_id(item.getKey());
                        ratingList.add(ratingContent);
                    }
                    listener.onLoadRatingSuccess(ratingList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void IsExistUserRating(String game_id) {
        mRef.child("rating/" + game_id + "/" + firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    listener.onUserRatingNotExist();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadUserImageLink() {
        mStorageRef.child("users/" + User.getInstance().getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                listener.onLoadUserImageLinkSuccess(uri.toString());
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
                    listener.onLoadGameImageLinkSuccess(uri.toString());
                }
            });
        }
    }

    public void createNewRating(String game_id, RatingContent ratingContent) {
        mRef.child("rating/" + game_id + "/" + firebaseUser.getUid()).setValue(ratingContent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onAddNewRatingSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
