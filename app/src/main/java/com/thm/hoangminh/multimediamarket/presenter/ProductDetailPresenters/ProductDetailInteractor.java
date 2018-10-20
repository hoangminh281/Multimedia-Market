package com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenters;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
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
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.callback.ProductDetailListener;

import java.io.File;
import java.io.IOException;
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
        mRef.child("product_detail/" + product_id).addValueEventListener(new ValueEventListener() {
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

    public void LoadProductTransactionHistory(String cate_id, String product_id) {
        mRef.child("purchased_product/" + firebaseUser.getUid() + "/" + cate_id + "/" + product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onLoadProductTransactionHistorySuccess();
                else listener.onLoadProductTransactionHistoryFailure();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onLoadProductTransactionHistoryFailure();
            }
        });
    }

    public void LoadRating(final String product_id) {
        mRef.child("rating/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<ProductRating> ratingList = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        ProductRating productRating = item.getValue(ProductRating.class);
                        productRating.setUserId(item.getKey());
                        productRating.setProductId(product_id);
                        ratingList.add(productRating);
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

    public void createNewRating(final String product_id, ProductRating productRating) {
        mRef.child("rating/" + product_id + "/" + firebaseUser.getUid()).setValue(productRating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mRef.child("rating/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                            int[] ratingArr = {0, 0, 0, 0, 0};
                            for (DataSnapshot item : iterable) {
                                ProductRating rating = item.getValue(ProductRating.class);
                                ratingArr[rating.getPoint() - 1]++;
                            }
                            double ratingPoint = (double) (5 * ratingArr[4] + 4 * ratingArr[3]
                                    + 3 * ratingArr[2] + 2 * ratingArr[1] + ratingArr[0]) / (ratingArr[4]
                                    + ratingArr[3] + ratingArr[2] + ratingArr[1] + ratingArr[0]);
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

    public synchronized void CheckoutProductbyId(final String cate_id, final String product_id, final String owner_id) {
        mRef.child("users/" + firebaseUser.getUid() + "/balance")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final double balance = dataSnapshot.getValue(double.class);
                    mRef.child("products/" + product_id + "/price")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final double price = dataSnapshot.getValue(double.class);
                                double refund = balance - price;
                                if (refund > -1) {
                                    mRef.child("users/" + firebaseUser.getUid() + "/balance").setValue(refund)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                            mRef.child("purchased_product/" + firebaseUser.getUid() + "/" + cate_id
                                                    + "/" + product_id + "/time")
                                                    .setValue(dateFormatter.format(Calendar.getInstance().getTime()))
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            mRef.child("users/" + owner_id + "/balance")
                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    mRef.child("users/" + owner_id + "/balance")
                                                                            .setValue(dataSnapshot.getValue(double.class) + price)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            mRef.child("product_detail/" + product_id + "/downloaded")
                                                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    mRef.child("product_detail/" + product_id + "/downloaded")
                                                                                            .setValue(dataSnapshot.getValue(int.class) + 1);
                                                                                }

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            });
                                                                            listener.onCheckoutProductbyIdSuccess();
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            listener.onCheckoutProductbyIdFailure();
                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
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

    public void ActiveProduct(String id, int status) {
        mRef.child("products/" + id + "/status").setValue(status);
    }

    public void FindCurrentUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("users/" + user.getUid() + "/role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onFindCurrentUserSuccess(dataSnapshot.getValue(int.class), user.getUid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void downLoadProduct(final Context context, final String fileName) {
        if (fileName == null || fileName.equals("")) {
            listener.onDownloadProductFailure();
            return;
        }
        String[] splitFileName = fileName.split("\\.");
        final File localFile;
        try {
            if (splitFileName.length == 1)
                localFile = File.createTempFile(splitFileName[0], "");
            else
                localFile = File.createTempFile(splitFileName[0], "." + splitFileName[1]);
        } catch (IOException e) {
            listener.onDownloadProductFailure();
            return;
        }
        if (localFile == null) {
            listener.onDownloadProductFailure();
            return;
        }
        mStorageRef.child("files/" + fileName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS.toString(), fileName);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
                final Long refid = downloadManager.enqueue(request);
                BroadcastReceiver onComplete = new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        if (refid != 0)
                            listener.onDownloadProductSuccess();
                    }
                };
                context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onDownloadProductFailure();
            }
        });
    }

    public void LoadProductById(String product_id) {
        mRef.child("products/" + product_id).addValueEventListener(new ValueEventListener() {
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
}
