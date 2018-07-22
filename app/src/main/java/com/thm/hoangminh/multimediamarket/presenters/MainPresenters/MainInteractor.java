package com.thm.hoangminh.multimediamarket.presenters.MainPresenters;

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
import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainInteractor {

    private MainListener listener;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private FirebaseUser firebaseUser;

    public MainInteractor(MainListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void LoadUserProfile() {
        mRef.child("users/" + firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadUserProfileSuccess(dataSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadCategory() {
        mRef.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<Category> categories = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        categories.add(item.getValue(Category.class));
                    }
                    listener.onLoadCategorySuccess(categories);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadProductSuggestions() {
        mRef.child("products/").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> suggestions = new HashMap<>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot item : iterable) {
                    if (User.getInstance().getRole() == User.ADMIN || item.child("status").getValue(int.class) == 1) {
                        suggestions.put(item.getKey(), item.child("title").getValue(String.class));
                    }
                }
                listener.onLoadProductSuggestionsSuccess(suggestions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
