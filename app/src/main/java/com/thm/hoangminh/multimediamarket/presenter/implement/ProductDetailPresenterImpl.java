package com.thm.hoangminh.multimediamarket.presenter.service.implement;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenter;
import com.thm.hoangminh.multimediamarket.repository.BookmarkRepository;
import com.thm.hoangminh.multimediamarket.repository.FileStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.BookmarkRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.FileStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.PurchasedProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RatingRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.ProductDetailView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductDetailPresenterImpl implements ProductDetailListener {

    private ProductDetailView listener;
    private ProductDetailInteractor interactor;
    private double price;

    public ProductDetailPresenterImpl(ProductDetailView listener) {
        this.listener = listener;
        this.interactor = new ProductDetailInteractor(this);
    }

    public void LoadProductDetailById(String product_id) {
        interactor.LoadProductDetailById(product_id);
    }

    public void LoadBookmarkContent(String cate_id, String product_id) {
        interactor.LoadBookmarkContent(cate_id, product_id);
    }

    public void LoadProductTransactionHistory(String cate_id, String product_id) {
        interactor.LoadProductTransactionHistory(cate_id, product_id);
    }

    @Override
    public void onLoadProductTransactionHistorySuccess() {
        listener.EnableInstall();
    }

    public void LoadRating(String product_id) {
        interactor.LoadRating(product_id);
        interactor.IsExistUserRating(product_id);
    }

    @Override
    public void onLoadProductDetailByIdSuccess(ProductDetail productDetail) {
        listener.showProductDetail(productDetail);
        interactor.LoadOwnerById(productDetail.getOwnerId());
        if (productDetail.getImageList() != null) {
            interactor.getImageLinkDownload(new ArrayList<>(productDetail.getImageList().values()));
        }
    }

    @Override
    public void onMarkedContent(boolean b) {
        listener.checkBookmark(b);
    }

    @Override
    public void onLoadOwnerSuccess(User user) {
        listener.showOwner(user);
    }

    @Override
    public void setupCheckoutDialog(final double price) {
        listener.showProgressbarDialog();
        userRepository.findBalance(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double balance = dataSnapshot.getValue(double.class);
                    if (balance >= price) {
                        listener.enableCheckout(balance);
                    } else if (balance < price) {
                        listener.disableCheckout(balance);
                    }
                    listener.hideDialogProgressbar();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addNewRating(final String productId, float ratingPoint, String ratingContent) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        ProductRating productRating = new ProductRating(productId, (int) ratingPoint, ratingContent, dateFormatter.format(Calendar.getInstance().getTime()));
        ratingRepository.addWithEventListener(currentUser.getUid(), productRating,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        ratingRepository.findById(productId, new ValueEventListener() {
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
                                    productRepository.setRatingPoint(productId, ratingPoint);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        listener.showSuccessRatingLayout();
                    }
                }, null);
    }

    public void loadProductTransactionHistory(String cateId, String productId) {
        purchasedProductRepository.findAndWatchByUserIdAndCateIdAndProductId(currentUser.getUid(), cateId, productId,
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            listener.enableDownloading();
                        } else {
                            listener.enableBuying();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        listener.enableBuying();
                    }
                });
    }

    @Override
    public void checkoutProduct(final String cateId, final String productId, final String ownerId) {
        listener.showProgressbarDialog();
        // TODO: 29/9/2018
        userRepository.findBalance(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final double balance = dataSnapshot.getValue(double.class);
                    productRepository.findPriceByProductId(productId, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                final double price = dataSnapshot.getValue(double.class);
                                double refund = balance - price;
                                if (refund > -1) {
                                    userRepository.setBalance(currentUser.getUid(), refund, new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                            Product purcharsedProduct = new Product();
                                            purcharsedProduct.setCateId(cateId);
                                            purcharsedProduct.setProductId(productId);
                                            String time = dateFormatter.format(Calendar.getInstance().getTime());
                                            purchasedProductRepository.addWithEventListener(currentUser.getUid(), purcharsedProduct, time,
                                                    new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            userRepository.findBalance(ownerId, new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                        userRepository.setBalance(ownerId, dataSnapshot.getValue(double.class) + price, new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                productDetailRepository.findPurchasedQuantityByProductId(productId, new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.exists()) {
                                                                                            productDetailRepository.setPurchasedQuantityByProductId(productId, dataSnapshot.getValue(int.class) + 1, null, null);
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                                    }
                                                                                });
                                                                                listener.showMessage(R.string.info_buyingSuccess);
                                                                                listener.closeBuyingDialog();
                                                                            }
                                                                        }, new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                listener.showMessage(R.string.info_failure);
                                                                                listener.hideDialogProgressbar();
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    listener.showMessage(R.string.info_failure);
                                                                    listener.hideDialogProgressbar();
                                                                }
                                                            });
                                                        }
                                                    },
                                                    new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            listener.showMessage(R.string.info_failure);
                                                            listener.hideDialogProgressbar();
                                                        }
                                                    });
                                        }
                                    }, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.showMessage(R.string.info_failure);
                                            listener.hideDialogProgressbar();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.showMessage(R.string.info_failure);
                            listener.hideDialogProgressbar();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.showMessage(R.string.info_failure);
                listener.hideDialogProgressbar();
            }
        });
    }

    @Override
    public void onLoadUserImageLinkSuccess(String user_image_link) {
        listener.showImageUser(user_image_link);
    }

    @Override
    public void loadImageProduct(Context context, ImageView imgLogo, Product product) {
        Validate.validateImageProduct(imgLogo, product.getStatus());
        ImageLoader.loadImageProduct(productStorageRepository, context, imgLogo, product.getPhotoId());
    }
    @Override
    public void downLoadProduct(final Context context, final String fileName) {
        if (fileName == null || fileName.equals("")) {
            listener.showMessage(R.string.info_downloadFireFailure);
            listener.enableDownloading();
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
            listener.showMessage(R.string.info_downloadFireFailure);
            listener.enableDownloading();
            return;
        }
        if (localFile == null) {
            listener.showMessage(R.string.info_downloadFireFailure);
            listener.enableDownloading();
            return;
        }
        listener.hideDialogProgressbar();
    }

    public void CheckoutProduct(String cate_id, String product_id, String owner_id) {
        listener.showDialogProgressbar();
        interactor.CheckoutProductbyId(cate_id, product_id, owner_id);
    }

    @Override
    public void onCheckoutProductbyIdSuccess() {
        listener.showMessageFromResource(R.string.info_buyingSuccess);
        listener.closeDialogProgressbar();
    }

    @Override
    public void onCheckoutProductbyIdFailure() {
        listener.showMessageFromResource(R.string.infor_failure);
        listener.hideDialogProgressbar();
    }

    public void SavedProductBookmark(String cate_id, String product_id) {
        interactor.SavedProductBookmark(cate_id, product_id);
    }

    @Override
    public void onSavedProductBookmarkSuccess() {
        listener.showMessageFromResource(R.string.info_saved);
    }

    public void UnSavedProductBookmark(String cate_id, String product_id) {
        interactor.UnSavedProductBookmark(cate_id, product_id);
    }

    @Override
    public void onUnSavedProductBookmarkSuccess() {
        listener.showMessageFromResource(R.string.info_unSaved);
    }

    public void ActiveProduct(String id, int status) {
        interactor.ActiveProduct(id, status);
    }

    @Override
    public void onFindCurrentUserSuccess(int role, String user_id) {
        listener.onLoadCurrentUserSuccess(role, user_id);
    }

    @Override
    public void onDownloadProductSuccess() {
        listener.showMessage(R.string.info_downloadFileSuccess);
        listener.EnableInstall();
    }

    @Override
    public void onDownloadProductFailure() {
        listener.showMessage(R.string.info_downloadFireFailure);
        listener.EnableInstall();
    }

    @Override
    public void onLoadProductTransactionHistoryFailure() {
        listener.EnableButtonBuy();
    }

    @Override
    public void onLoadProductByIdSuccess(Product value) {
        listener.showProduct(value);
    }

    public void LoadCurrentUser() {
        interactor.FindCurrentUser();
    }

    public void downLoadProduct(Context context , String fileName) {
        interactor.downLoadProduct(context, fileName);
    }

    public void LoadProductById(String product_id) {
        interactor.LoadProductById(product_id);
    }
}
