package com.thm.hoangminh.multimediamarket.presenters.ProfilePresenters;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;


public class ProfileInteractor {
    private ProfileListener listener;
    private DatabaseReference mRef;
    private String user_id;
    private FirebaseUser currentUser;

    public ProfileInteractor(ProfileListener listener, String user_id) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (user_id.equals("")) {
            this.user_id = currentUser.getUid();
        } else this.user_id = user_id;
    }

    public void LoadCurrentUserInformation() {
        mRef.child("users/" + user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadCurrentUserInformationSuccess(dataSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadCurrentUserMultimedia() {
        mRef.child("purchased_product/" + user_id + "/" + MainActivity.categories.get(0).getCate_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    int size = 0;
                    for (DataSnapshot item : iterable) {
                        size++;
                    }
                    listener.onLoadCurrentUserProductSuccess(size);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.child("purchased_product/" + user_id + "/" + MainActivity.categories.get(1).getCate_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    int size = 0;
                    for (DataSnapshot item : iterable) {
                        size++;
                    }
                    listener.onLoadCurrentUserImageSuccess(size);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.child("purchased_product/" + user_id + "/" + MainActivity.categories.get(2).getCate_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    int size = 0;
                    for (DataSnapshot item : iterable) {
                        size++;
                    }
                    listener.onLoadCurrentUserVideoSuccess(size);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRef.child("purchased_product/" + user_id + "/" + MainActivity.categories.get(3).getCate_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    int size = 0;
                    for (DataSnapshot item : iterable) {
                        size++;
                    }
                    listener.onLoadCurrentUserMusicSuccess(size);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void CheckCurrentUserProvider() {
        if (user_id.equals(currentUser.getUid()))
            listener.onCheckCurrentUserProviderSuccess(currentUser.getProviders().get(0));
    }

    public void updateBalanceByUserId(double balance) {
        mRef.child("users/" + user_id + "/balance").setValue(balance).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onEditSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onExistError();
            }
        });
    }

    public void EditUsername(String username) {
        mRef.child("users/" + user_id + "/name").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onEditSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onExistError();
            }
        });
    }

    public void EditEmail(final String email) {
        currentUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.child("users/" + user_id + "/email").setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.onEditSuccess();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onExistError();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EditEmail", e.getMessage());
            }
        });
    }

    public void EditPassword(String password) {
        currentUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onEditSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onExistError();
            }
        });
    }

    public void EditBirthday(int year, int month, int day) {
        mRef.child("users/" + user_id + "/birthday").setValue(day + "/" + month + "/" + year).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onEditSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onExistError();
            }
        });
    }

    public void EditGender(int i) {
        mRef.child("users/" + user_id + "/sex").setValue(i).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onEditSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onExistError();
            }
        });
    }

}
