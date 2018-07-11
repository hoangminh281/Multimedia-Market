package com.thm.hoangminh.multimediamarket.presenters.ProfilePresenters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.models.User;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class ProfileInteractor {
    private ProfileListener listener;
    private FirebaseUser user;
    private DatabaseReference mRef;

    public ProfileInteractor(ProfileListener listener) {
        this.listener = listener;
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void LoadCurrentUserInformation() {
        mRef.child("users/" + user.getUid()).addValueEventListener(new ValueEventListener() {
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
        mRef.child("purchased_product/" + user.getUid()).addValueEventListener(new ValueEventListener() {
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
        mRef.child("purchased_image/" + user.getUid()).addValueEventListener(new ValueEventListener() {
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
        mRef.child("purchased_video/" + user.getUid()).addValueEventListener(new ValueEventListener() {
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
        mRef.child("purchased_music/" + user.getUid()).addValueEventListener(new ValueEventListener() {
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
        listener.onCheckCurrentUserProviderSuccess(user.getProviders().get(0));
    }

    public void EditUsername(String username) {
        mRef.child("users/" + user.getUid() + "/name").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.child("users/" + user.getUid() + "/email").setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onEditSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onExistError();
                Log.d("EditEmail", e.getMessage());
            }
        });
    }

    public void EditBirthday(int year, int month, int day) {
        mRef.child("users/" + user.getUid() + "/birthday").setValue(day + "/" + month + "/" + year).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        mRef.child("users/" + user.getUid() + "/sex").setValue(i).addOnSuccessListener(new OnSuccessListener<Void>() {
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
