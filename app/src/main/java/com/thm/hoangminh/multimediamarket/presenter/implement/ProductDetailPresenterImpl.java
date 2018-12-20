package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductBookmark;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.model.PurchasedProduct;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenter;
import com.thm.hoangminh.multimediamarket.repository.BookmarkRepository;
import com.thm.hoangminh.multimediamarket.repository.FileStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.base.StorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.BookmarkRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.FileStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.PurchasedProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RatingRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.ProductDetailView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.Callable;

public class ProductDetailPresenterImpl implements ProductDetailPresenter {
    private Product product;
    private User dbCurrentUser;
    private FirebaseUser currentUser;
    private ProductDetailView listener;
    private ProductDetail productDetail;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private ProductRepository productRepository;
    private BookmarkRepository bookmarkRepository;
    private StorageRepository productStorageRepository;
    private FileStorageRepository fileStorageRepository;
    private ProductDetailRepository productDetailRepository;
    private PurchasedProductRepository purchasedProductRepository;

    public ProductDetailPresenterImpl(ProductDetailView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        ratingRepository = new RatingRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        bookmarkRepository = new BookmarkRepositoryImpl();
        fileStorageRepository = new FileStorageRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productDetailRepository = new ProductDetailRepositoryImpl();
        productStorageRepository = new ProductStorageRepositoryImpl();
        purchasedProductRepository = new PurchasedProductRepositoryImpl();
    }

    @Override
    public void extractBundle(Bundle bundle) {
        String cateId = bundle.getString(Constants.CateIdKey);
        String productId = bundle.getString(Constants.ProductIdKey);

        bindCurrentUser();
        loadProduct(productId);
        loadProductDetail(productId);
        loadProductBookmark(cateId, productId);
        checkPurchasedProductHistory(cateId, productId);
        loadProductRating(productId);
        checkUserRating(productId);
    }

    private void bindCurrentUser() {
        userRepository.findAndWatchById(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dbCurrentUser = dataSnapshot.getValue(User.class);
                    validateProduct();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void loadUserWallet() {
        listener.showProgressbarDialog();
        userRepository.findBalance(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double balance = dataSnapshot.getValue(double.class);
                    listener.enableOrDisableProductCheckout(balance, balance >= product.getPrice());
                    listener.hideProgressbarDialog();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void loadProduct(String productId) {
        productRepository.findAndWatchById(productId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    product = dataSnapshot.getValue(Product.class);
                    listener.showProduct(product);
                    validateProduct();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductDetail(String productId) {
        productDetailRepository.findById(productId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productDetail = dataSnapshot.getValue(ProductDetail.class);
                    listener.showProductDetail(productDetail);
                    validateProduct();
                    loadProductOwner(productDetail.getOwnerId());
                    if (productDetail.getImageIdList() != null) {
                        loadImageList(new ArrayList<>(productDetail.getImageIdList().values()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductOwner(String ownerId) {
        userRepository.findById(ownerId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.showOwner(dataSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validateProduct() {
        if (product == null || currentUser.getUid() == null || productDetail == null
                || dbCurrentUser == null) return;
        String userId = currentUser.getUid();
        String ownerId = productDetail.getOwnerId();
        int currentUserRole = dbCurrentUser.getRole();
        int productStatus = product.getStatus();
        if (ownerId.equals(userId) || currentUserRole == Constants.AdminRole) {
            listener.setVisibleItemMenu(R.id.menu_update, true);
            if (productStatus == Constants.ProductDisable) {
                listener.setVisibleItemMenu(R.id.menu_active, true);
                listener.setVisibleItemMenu(R.id.menu_deactive, false);
            } else {
                listener.setVisibleItemMenu(R.id.menu_active, false);
                listener.setVisibleItemMenu(R.id.menu_deactive, true);
            }
        } else {
            listener.setVisibleItemMenu(R.id.menu_update, false);
            listener.setVisibleItemMenu(R.id.menu_active, false);
            listener.setVisibleItemMenu(R.id.menu_deactive, false);
        }
    }

    private void loadImageList(ArrayList<String> imageIdList) {
        if (imageIdList != null) {
            for (String imageId : imageIdList) {
                productStorageRepository.findUriById(imageId, new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        listener.addUriToSliderLayout(uri);
                        listener.refreshSliderAdapter();
                    }
                }, null);
            }
        }
    }

    private void loadProductBookmark(String cateId, String productId) {
        ProductBookmark productBookmark = new ProductBookmark();
        productBookmark.setUserId(currentUser.getUid());
        productBookmark.setCateId(cateId);
        productBookmark.setProductId(productId);
        bookmarkRepository.findByProductBookmark(productBookmark, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ProductBookmark productBookmark = null;
                if (dataSnapshot.exists()) {
                    productBookmark = dataSnapshot.getValue(ProductBookmark.class);
                }
                listener.activeOrDeactiveBookmark(productBookmark == null || productBookmark.getValue() == Constants.BookMarkDisable ? false : true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkPurchasedProductHistory(String cateId, String productId) {
        purchasedProductRepository.findAndWatch(currentUser.getUid(), cateId, productId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.enableOrDisableDownloadButton(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductRating(final String productId) {
        ratingRepository.findById(productId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<ProductRating> ratingList = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        ProductRating productRating = item.getValue(ProductRating.class);
                        productRating.setUserId(item.getKey());
                        productRating.setProductId(productId);
                        ratingList.add(productRating);
                    }
                    listener.showRatingFragment(ratingList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkUserRating(String productId) {
        ratingRepository.findByUserId(currentUser.getUid(), productId,
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listener.showOrHideRatingLayout(dataSnapshot.exists() ? true : false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void activeOrDeactiveProduct(boolean b) {
        productRepository.setStatus(product.getProductId(), b ? Constants.ProductEnable : Constants.ProductDisable, null, null);
    }

    @Override
    public void enableOrDisableBookmark(final boolean b) {
        ProductBookmark productBookmark = new ProductBookmark();
        productBookmark.setCateId(product.getCateId());
        productBookmark.setProductId(product.getProductId());
        productBookmark.setUserId(currentUser.getUid());
        productBookmark.setValue(b ? Constants.BookMarkEnable : Constants.BookMarkDisable);
        bookmarkRepository.add(productBookmark, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                listener.showMessage(b ? R.string.info_saved : R.string.info_unSaved);
            }
        }, null);
    }

    @Override
    public void checkoutProduct() {
        listener.showProgressbarDialog();
        // TODO: 29/9/2018
        userRepository.findBalance(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    final double balance = dataSnapshot.getValue(double.class);
                    productRepository.findPriceByProductId(product.getProductId(), new ValueEventListener() {
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
                                            String currentDateTime = dateFormatter.format(Calendar.getInstance().getTime());
                                            PurchasedProduct purcharsedProduct = new PurchasedProduct(product.getCateId(), product.getProductId()
                                                    , currentUser.getUid(), currentDateTime);
                                            purchasedProductRepository.add(purcharsedProduct,
                                                    new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            //charge owner product
                                                            userRepository.findBalance(productDetail.getOwnerId(), new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                        userRepository.setBalance(productDetail.getOwnerId(), (dataSnapshot.getValue(double.class) + ((int) (price * (100 - Constants.ChargeFeePer) / 100) * 100) / 100.00), null, null);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });
                                                            //get profit to admin
                                                            userRepository.findBalance(Constants.AdminId, new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                        userRepository.setBalance(Constants.AdminId, (dataSnapshot.getValue(double.class) + ((int) (price * Constants.ChargeFeePer / 100) * 100) / 100.00), null, null);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                }
                                                            });
                                                            //count purchased product
                                                            productDetailRepository.findPurchasedQuantityByProductId(product.getProductId(), new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (dataSnapshot.exists()) {
                                                                        productDetailRepository.setPurchasedQuantityByProductId(product.getProductId(), dataSnapshot.getValue(int.class) + 1, null, null);
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                            listener.showMessage(R.string.info_buyingSuccess);
                                                            listener.closeCheckoutDialog();
                                                        }
                                                    },
                                                    new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            listener.showMessage(R.string.info_failure);
                                                            listener.hideProgressbarDialog();
                                                        }
                                                    });
                                        }
                                    }, new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            listener.showMessage(R.string.info_failure);
                                            listener.hideProgressbarDialog();
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            listener.showMessage(R.string.info_failure);
                            listener.hideProgressbarDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.showMessage(R.string.info_failure);
                listener.hideProgressbarDialog();
            }
        });
    }

    @Override
    public void addRating(ProductRating productRating) {
        productRating.setUserId(currentUser.getUid());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        productRating.setTime(dateFormatter.format(Calendar.getInstance().getTime()));
        ratingRepository.add(productRating, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.showSuccessRatingLayout();
                ratingRepository.findById(product.getProductId(), new ValueEventListener() {
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
                            productRepository.setRatingPoint(product.getProductId(), ratingPoint, null, null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }, null);
    }

    @Override
    public void downLoadProduct(final Context context) {
        listener.enableOrDisableDownloadButton(false);
        String fileId = productDetail.getFileId();
        if (fileId == null || fileId.equals("")) {
            listener.showMessage(R.string.info_downloadFireFailure);
            listener.enableOrDisableDownloadButton(true);
            return;
        }
        fileStorageRepository.downloadFile(fileId, context,
                new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        listener.showMessage(R.string.info_downloadFileSuccess);
                        fileStorageRepository.unRegisterReceiver(context);
                        listener.enableOrDisableDownloadButton(true);
                        return null;
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.info_downloadFireFailure);
                        listener.enableOrDisableDownloadButton(true);
                    }
                });
    }
}
