package com.thm.hoangminh.multimediamarket.presenters.UpdateProductPresenters;

import android.graphics.Bitmap;
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

public class UpdateProductInteractorImpl implements UpdateProductInteractor {
    private final DatabaseReference mRef;
    private final StorageReference mStorageRef;
    private final FirebaseUser user;
    private UpdateProductListener listener;

    public UpdateProductInteractorImpl(UpdateProductListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void LoadProductById(String id) {
        mRef.child("products/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onLoadProductByIdSuccess(dataSnapshot.getValue(Product.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void LoadProductDetailById(String id) {
        mRef.child("product_detail/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onLoadProductDetailByIdSuccess(dataSnapshot.getValue(ProductDetail.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void LoadSectionByProductId(String cate_id, final String id) {
        mRef.child("sections/" + cate_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    Map<String, String> sections = new HashMap<>();
                    for (DataSnapshot item : iterable) {
                        if (item.child("product_id/" + id).exists()) {
                            sections.put(item.getKey(), item.child("title").getValue(String.class));
                        }
                    }
                    listener.onLoadSectionByProductIdSuccess(sections);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void LoadSectionById(String cate_id) {
        mRef.child("sections/" + cate_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    Map<String, String> map = new HashMap<>();
                    for (DataSnapshot item : iterable) {
                        map.put(item.getKey(), item.child("title").getValue(String.class));
                    }
                    listener.onLoadSectionByIdSuccess(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void UpdateProductImage(final String product_id, String image_old_id, final Bitmap bitmap) {
        mStorageRef.child("products/" + image_old_id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final String id = Tools.createImageNameRandom();
                mRef.child("products/" + product_id + "/photoId").setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = mStorageRef.child("products/" + id).putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                listener.onUpdateProductImageSuccess();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.onUpdateProductImageFailure(e.getMessage());
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void UpdateProductDetailImage(final String product_id, final Map<String, String> imageOldIds, final Map<Integer, Bitmap> bitmaps) {
        Integer[] newBitmapIndexs = bitmaps.keySet().toArray(new Integer[bitmaps.size()]);
        final Bitmap[] newBitmaps = bitmaps.values().toArray(new Bitmap[bitmaps.size()]);
        final String[] oldImageKeys = imageOldIds.keySet().toArray(new String[imageOldIds.size()]);
        String[] oldImageValues = imageOldIds.values().toArray(new String[imageOldIds.size()]);
        for (int i = 0; i < bitmaps.size(); i++) {
            final int finalI = i;
            final String id = Tools.createImageNameRandom();
            final int index = newBitmapIndexs[i];
            if (index < imageOldIds.size()) {
                mStorageRef.child("products/" + oldImageValues[index]).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mRef.child("product_detail/" + product_id + "/imageList/" + oldImageKeys[index]).setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                newBitmaps[finalI].compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] data = baos.toByteArray();
                                UploadTask uploadTask = mStorageRef.child("products/" + id).putBytes(data);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        listener.onUpdateProductDetailImageSuccess(finalI);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onUpdateProductDetailImageFailure(finalI, e.getMessage());
                                    }
                                });
                            }
                        });
                    }
                });
            } else {
                mRef.child("product_detail/" + product_id + "/imageList").push().setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        newBitmaps[finalI].compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();
                        UploadTask uploadTask = mStorageRef.child("products/" + id).putBytes(data);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                listener.onUpdateProductDetailImageSuccess(finalI);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.onUpdateProductDetailImageFailure(finalI, e.getMessage());
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void UpdateProduct(Product product) {
        mRef.child("products/" + product.getProduct_id()).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onUpdateProductSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUpdateProductFailure(e.getMessage());
            }
        });
    }

    @Override
    public void UpdateProductDetail(ProductDetail productDetail) {
        mRef.child("product_detail/" + productDetail.getId()).setValue(productDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onUpdateProductDetailSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUpdateProductFailure(e.getMessage());
            }
        });
    }

    @Override
    public void DeleteProductSections(String product_id, String
            cate_id, ArrayList<String> sectionOldIds) {
        for (String section : sectionOldIds) {
            mRef.child("sections/" + cate_id + "/" + section + "/product_id/" + product_id).setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onDeleteProductSectionsSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onDeleteProductSectionsFailure(e.getMessage());
                }
            });
        }
    }

    @Override
    public void UpdateProductSections(String product_id, String
            cate_id, ArrayList<String> sectionNewIds) {
        for (String section : sectionNewIds) {
            mRef.child("sections/" + cate_id + "/" + section + "/product_id/" + product_id).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listener.onUpdateProductSectionsSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onUpdateProductSectionsFailure(e.getMessage());
                }
            });
        }
    }

    @Override
    public void UpdateFile(final String product_id, final String oldFileId, final File file) {
        mStorageRef.child("files/" + oldFileId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                final String id = Tools.createFileNameRandom(file.getName());
                mRef.child("product_detail/" + product_id + "/downloadLink").setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mRef.child("product_detail/" + product_id + "/capacity").setValue(file.getSize().getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                StorageReference riversRef = mStorageRef.child("files/" + id);
                                UploadTask uploadTask = riversRef.putFile(file.getUri());

// Register observers to listen for when the download is done or if it fails
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        listener.onUpdateFileFailure(exception.getMessage());
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        listener.onUploadFileSuccess();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.onUpdateFileFailure(e.getMessage());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onUpdateFileFailure(e.getMessage());
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //check old file exist
                mStorageRef.child("files/" + oldFileId).getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        final String id = Tools.createFileNameRandom(file.getName());
                        mRef.child("product_detail/" + product_id + "/downloadLink").setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mRef.child("product_detail/" + product_id + "/capacity").setValue(file.getSize().getValue()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        StorageReference riversRef = mStorageRef.child("files/" + id);
                                        UploadTask uploadTask = riversRef.putFile(file.getUri());

// Register observers to listen for when the download is done or if it fails
                                        uploadTask.addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                listener.onUpdateFileFailure(exception.getMessage());
                                            }
                                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                listener.onUploadFileSuccess();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onUpdateFileFailure(e.getMessage());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.onUpdateFileFailure(e.getMessage());
                            }
                        });
                    }
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.onUpdateFileFailure("");
                    }
                });
            }
        });
    }

}
