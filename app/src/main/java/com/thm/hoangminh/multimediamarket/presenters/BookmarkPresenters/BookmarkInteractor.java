package com.thm.hoangminh.multimediamarket.presenters.BookmarkPresenters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookmarkInteractor {
    private BookmarkListener listener;
    private DatabaseReference mRef;
    private ValueEventListener mListener;

    public BookmarkInteractor(BookmarkListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void findCurrentUserRole() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mListener = mRef.child("users/" + currentUser.getUid() + "/role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onFindCurrentUserRoleSuccess(dataSnapshot.getValue(int.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void RemoveListener() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("users/" + currentUser.getUid() + "/role").removeEventListener(mListener);
    }
}
