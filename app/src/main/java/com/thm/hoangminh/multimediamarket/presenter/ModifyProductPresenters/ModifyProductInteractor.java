package com.thm.hoangminh.multimediamarket.presenters.ModifyProductPresenters;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
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
import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.references.Tools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModifyProductInteractor {
    private ModifyProductListener listener;
    private DatabaseReference mRef;
    private FirebaseUser currentUser;
    private StorageReference mStorageRef;

    public ModifyProductInteractor(ModifyProductListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public void LoadCategoryProduct(String key_category) {
        mRef.child("sections/" + key_category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> category = new HashMap<>();
                    Iterable<DataSnapshot> sections = dataSnapshot.getChildren();
                    for (DataSnapshot item : sections) {
                        Section section = item.getValue(Section.class);
                        category.put(section.getSection_id(), section.getTitle());
                    }
                    listener.LoadCategoryProductSuccess(category);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void CreateNewProduct(String title, String key_cate, String photo_id, double price, final String intro, final String desc, final int age_limit, final String video, final File file) {
        DatabaseReference mRefTmp = mRef.child("products").push();
        final String id = mRefTmp.getKey();
        mRefTmp.setValue(new Product(id, key_cate, title, photo_id, 5, price, 1)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onCreateNewProductSuccess(id, intro, desc, file, age_limit, currentUser.getUid(), video);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onCreateNewProductFailure();
            }
        });
    }

    public void CreateProductDetailById(final ProductDetail pDetail, final ArrayList<String> photoNames) {
        mRef.child("product_detail/" + pDetail.getId()).setValue(pDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onUploadPhotoSucceed(0);
                for (int i = 1; i < photoNames.size(); i++) {
                    final int finalI = i;
                    mRef.child("product_detail/" + pDetail.getId() + "/imageList").push().setValue(photoNames.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.onUploadPhotoSucceed(finalI);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mRef.child("products/" + pDetail.getId()).removeValue();
                listener.onCreateNewProductFailure();
            }
        });
    }

    public void UploadPhoto(final int photoIndex, final String photoId, Bitmap photo) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child("products/" + photoId).putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onUploadPhotoSuccess(photoIndex);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUploadPhotoFailure(photoIndex);
            }
        });
    }

    public void UploadFile(File file) {
        StorageReference riversRef = mStorageRef.child("files/" + file.getName());
        UploadTask uploadTask = riversRef.putFile(file.getUri());
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onUploadFileSuccess();
            }
        });
    }

    public void UpdateSection(String cate_id, Map<String, String> sections, String id) {
        for (final Map.Entry section : sections.entrySet()) {
            mRef.child("sections/" + cate_id + "/" + section.getKey() + "/product_id/" + id).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onUpdateSectionSuccess((String) section.getValue());
                }
            });
        }
    }
}
