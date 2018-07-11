package com.thm.hoangminh.multimediamarket.presenters.ProductDetailPresenters;

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
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductDetailInteractor {

    private ProductDetailListener listener;
    private DatabaseReference mRef;
    private StorageReference mStorageRef;
    private FirebaseUser firebaseUser;

    public ProductDetailInteractor(ProductDetailListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void LoadProductDetailById(String product_id) {
        mRef.child("product_detail/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadProductDetailByIdSuccess(dataSnapshot.getValue(ProductDetail.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadBookmarkContent(String cate_id, String product_id) {
        mRef.child("bookmark/" + firebaseUser.getUid() + "/" + cate_id + "/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                if (dataSnapshot.exists()) {
                    i = dataSnapshot.getValue(int.class);
                }
                listener.onMarkedContent(i == 0 ? false : true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadProductTransactionHistory(String product_id) {
        mRef.child("purchased_product/" + firebaseUser.getUid() + "/" + product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadProductTransactionHistorySuccess();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadRating(final String product_id) {
        mRef.child("rating/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<RatingContent> ratingList = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        RatingContent ratingContent = item.getValue(RatingContent.class);
                        ratingContent.setUser_id(item.getKey());
                        ratingContent.setContent_id(product_id);
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

    public void IsExistUserRating(String product_id) {
        mRef.child("rating/" + product_id + "/" + firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        mRef.child("users/" + owner_id).addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void getImageLinkDownload(ArrayList<String> imageList) {
        for (String img : imageList) {
            mStorageRef.child("products/" + img).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    listener.onLoadProductImageLinkSuccess(uri.toString());
                }
            });
        }
    }

    public void createNewRating(final String product_id, RatingContent ratingContent) {
        mRef.child("rating/" + product_id + "/" + firebaseUser.getUid()).setValue(ratingContent).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.child("rating/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                            int[] ratingArr = {0, 0, 0, 0, 0};
                            for (DataSnapshot item : iterable) {
                                RatingContent rating = item.getValue(RatingContent.class);
                                ratingArr[rating.getPoint() - 1]++;
                            }
                            double ratingPoint = (double) (5 * ratingArr[4] + 4 * ratingArr[3] + 3 * ratingArr[2] + 2 * ratingArr[1] + ratingArr[0]) / (ratingArr[4] + ratingArr[3] + ratingArr[2] + ratingArr[1] + ratingArr[0]);
                            ratingPoint *= 10;
                            ratingPoint = Math.round(ratingPoint);
                            ratingPoint /= 10;
                            mRef.child("products/" + product_id + "/rating").setValue(ratingPoint);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                listener.onAddNewRatingSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void LoadUserWallet() {
        mRef.child("users/" + firebaseUser.getUid() + "/balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.onLoadUserWalletSuccess(dataSnapshot.getValue(double.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public synchronized void CheckoutProductbyId(final String product_id) {
        mRef.child("users/" + firebaseUser.getUid() + "/balance").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final double balance = dataSnapshot.getValue(double.class);
                    mRef.child("products/" + product_id + "/price").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                double price = dataSnapshot.getValue(double.class);
                                double refund = balance - price;
                                if (refund > -1) {
                                    mRef.child("users/" + firebaseUser.getUid() + "/balance").setValue(refund).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

                                            mRef.child("purchased_product/" + firebaseUser.getUid() + "/" + product_id + "/time")
                                                    .setValue(dateFormatter.format(Calendar.getInstance().getTime()))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            listener.onCheckoutProductbyIdSuccess();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    listener.onCheckoutProductbyIdFailure();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.onCheckoutProductbyIdFailure();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.onCheckoutProductbyIdFailure();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCheckoutProductbyIdFailure();
            }
        });
    }

    public void SavedProductBookmark(String cate_id, String product_id) {
        mRef.child("bookmark/" + firebaseUser.getUid() + "/" + cate_id + "/" + product_id).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSavedProductBookmarkSuccess();
            }
        });
    }

    public void UnSavedProductBookmark(String cate_id, String product_id) {
        mRef.child("bookmark/" + firebaseUser.getUid() + "/" + cate_id + "/" + product_id).setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onUnSavedProductBookmarkSuccess();
            }
        });
    }

    public void DeleteProduct(String id) {
        mRef.child("products/" + id + "/status").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onDeleteProductSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onDeleteProductFailure(e);
            }
        });
    }
}
