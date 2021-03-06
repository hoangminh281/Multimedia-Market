package com.thm.hoangminh.multimediamarket.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.views.ProfileViews.ProfileView;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by Dell on 5/7/2018.
 */

public class User {
    private String id, name, image, email, phone, birthday;
    private int sex; // 0: male, 1: female
    private double balance;
    private int role; // 0: Admin, 1: mod, 2: user
    private int status; //0: inactive, 1: active

    public static final int ADMIN = 0;
    public static final int MOD = 1;
    public static final int USER = 2;

    private final String DEFAULT_IMG_KEY = "user.png";

    private static User instance;

    public static User getInstance() {
        if (instance == null) instance = new User();
        return instance;
    }

    public static User setInstance(User user) {
        instance = new User(user);
        return instance;
    }

    public User() {
    }

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.image = user.image;
        this.email = user.email;
        this.phone = user.phone;
        this.birthday = user.birthday;
        this.sex = user.sex;
        this.balance = user.balance;
        this.role = user.role;
    }

    public User(String name, String image, String email, String phone, String birthday, int sex, int role, int balance, int status) {
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.balance = balance;
        this.role = role;
        this.status = status;
    }

    public User(String id, String name, String image, String email, String phone, String birthday, int sex, double balance, int role, int status) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.sex = sex;
        this.balance = balance;
        this.role = role;
        this.status = status;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void createUserOnFirebase(final Context context) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();


    }

    public void LoadUserImageView(final ImageView img, final Context context) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef.child("users/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .error(R.mipmap.icon_app_2)
                        .into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void LoadUserRole(final TextView txt) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("roles/" + role).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    txt.setText(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadUserRoleWithColor(final TextView txt, final Context context) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("roles/" + role).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String roleName = dataSnapshot.getValue(String.class);
                    txt.setText(roleName);
                    switch (role) {
                        case ADMIN:
                            txt.setTextColor(context.getResources().getColor(R.color.red));
                            break;
                        case MOD:
                            txt.setTextColor(context.getResources().getColor(R.color.green));
                            break;
                        case USER:
                            txt.setTextColor(context.getResources().getColor(R.color.purple_50));
                            break;
                    }
                    mRef.child("users/" + id + "/role").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                                switch (dataSnapshot.getValue(int.class)) {
                                    case ADMIN:
                                        txt.setText("admin");
                                        txt.setTextColor(context.getResources().getColor(R.color.red));
                                        break;
                                    case MOD:
                                        txt.setText("mod");
                                        txt.setTextColor(context.getResources().getColor(R.color.green));
                                        break;
                                    case USER:
                                        txt.setText("user");
                                        txt.setTextColor(context.getResources().getColor(R.color.purple_50));
                                        break;
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadUserImageGender(ImageView img) {
        if (sex == 0) {
            img.setImageResource(R.mipmap.ic_male);
        } else if (sex == 1) {
            img.setImageResource(R.mipmap.ic_female);
        } else {
            img.setImageResource(R.mipmap.ic_male_female);
        }
    }

    public void UpdateImageCurrentUser(final Context context, Bitmap bitmap) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        final StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

        final String newImage = "image" + Calendar.getInstance().getTimeInMillis() + ".png";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        final byte[] data = baos.toByteArray();

        if (image.equals(DEFAULT_IMG_KEY)) {
            mStorageRef.child("users/" + newImage).putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mRef.child("users/" + id + "/image").setValue(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, R.string.info_editSuccess, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mStorageRef.child("users/" + newImage).delete();
                            Toast.makeText(context, R.string.infor_failure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, R.string.infor_failure, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mStorageRef.child("users/" + image).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mStorageRef.child("users/" + newImage).putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mRef.child("users/" + id + "/image").setValue(newImage).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, R.string.info_editSuccess, Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mStorageRef.child("users/" + newImage).delete();
                                    Toast.makeText(context, R.string.infor_failure, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, R.string.infor_failure, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void LoadUserStatus(final ImageView imgStatus) {
        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("users/" + id + "/status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    status = dataSnapshot.getValue(int.class);
                    if (status == 1)
                        imgStatus.setColorFilter(R.color.theme_app);
                    else imgStatus.clearColorFilter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
