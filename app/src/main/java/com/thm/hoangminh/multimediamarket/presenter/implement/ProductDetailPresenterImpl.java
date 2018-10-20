package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

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
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenter;
import com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenters.ProductDetailInteractor;
import com.thm.hoangminh.multimediamarket.repository.BookmarkRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.base.StorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.BookmarkRepositoryImpl;
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

public class ProductDetailPresenterImpl implements ProductDetailPresenter {
    private ProductDetailInteractor interactor;
    private User ownerUser;
    private Product product;
    private FirebaseUser currentUser;
    private ProductDetailView listener;
    private ProductDetail productDetail;
    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private ProductRepository productRepository;
    private BookmarkRepository bookmarkRepository;
    private StorageRepository productStorageRepository;
    private ProductDetailRepository productDetailRepository;
    private PurchasedProductRepository purchasedProductRepository;

    public ProductDetailPresenterImpl(ProductDetailView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        ratingRepository = new RatingRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productDetailRepository = new ProductDetailRepositoryImpl();
        bookmarkRepository = new BookmarkRepositoryImpl();
        productStorageRepository = new ProductStorageRepositoryImpl();
        purchasedProductRepository = new PurchasedProductRepositoryImpl();
        this.interactor = new ProductDetailInteractor(this);
    }

    @Override
    public void extractBundle(Bundle bundle) {
        String cateId = bundle.getString(Constants.CateIdKey);
        String productId = bundle.getString(Constants.ProductIdKey);

        loadProduct(productId);
        loadProductDetail(productId);
        loadProductBookmark(cateId, productId);
        checkPurchasedProductHistory(cateId, productId);
        loadProductRating(productId);
        checkUserRating(productId);
    }

    @Override
    public void activeOrDeactiveProduct(boolean b) {

    }

    @Override
    public void enableOrDisableBookmark(boolean b) {

    }

    @Override
    public void loadUserWallet() {
        listener.showDialogProgressbar();
        userRepository.findBalance(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double balance = dataSnapshot.getValue(double.class);
                    listener.enableOrDisableProductCheckout(balance, balance >= product.getPrice());
                    listener.hideDialogProgressbar();
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
                productDetail = dataSnapshot.getValue(ProductDetail.class);
                listener.showProductDetail(productDetail);
                validateProduct();
                loadProductOwner(productDetail.getOwnerId());
                loadImageList(new ArrayList<>(productDetail.getImageIdList().values()));
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
                    ownerUser = dataSnapshot.getValue(User.class);
                    listener.showOwner(ownerUser);
                    validateProduct();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void validateProduct() {
        if (product == null || currentUser.getUid() == null || productDetail == null
                || ownerUser == null) return;
        String userId = currentUser.getUid();
        String ownerId = productDetail.getOwnerId();
        int currentUserRole = ownerUser.getRole();
        int productStatus = product.getStatus();
        if (ownerId.equals(userId) || currentUserRole == User.ADMIN) {
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
                int i = 0;
                if (dataSnapshot.exists()) {
                    i = dataSnapshot.getValue(int.class);
                }
                listener.activeOrDeactiveBookmark(i == 0 ? false : true);
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
        ratingRepository.findAndWatchByUserId(currentUser.getUid(), productId,
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
    public void checkoutProduct() {
        listener.showDialogProgressbar();
        interactor.CheckoutProductbyId(cate_id, product_id, owner_id);
    }

    @Override
    public void downLoadProduct() {

    }

    @Override
    public void addRating(ProductRating productRating) {
        productRating.setUserId(currentUser.getUid());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        productRating.setTime(dateFormatter.format(Calendar.getInstance().getTime()));
        ratingRepository.add(productRating, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
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
                            productRepository.setRatingPoint(product.getProductId(), ratingPoint);
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
    @Override
    public void onCheckoutProductbyIdSuccess() {
        listener.showMessage(R.string.info_buyingSuccess);
        listener.closeDialogProgressbar();
    }

    @Override
    public void onCheckoutProductbyIdFailure() {
        listener.showMessage(R.string.infor_failure);
        listener.hideDialogProgressbar();
    }

    public void SavedProductBookmark(String cate_id, String product_id) {
        interactor.SavedProductBookmark(cate_id, product_id);
    }

    @Override
    public void onSavedProductBookmarkSuccess() {
        listener.showMessage(R.string.info_saved);
    }

    public void UnSavedProductBookmark(String cate_id, String product_id) {
        interactor.UnSavedProductBookmark(cate_id, product_id);
    }

    @Override
    public void onUnSavedProductBookmarkSuccess() {
        listener.showMessage(R.string.info_unSaved);
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
        listener.enableDownload();
    }

    @Override
    public void onDownloadProductFailure() {
        listener.showMessage(R.string.info_downloadFireFailure);
        listener.enableDownload();
    }

    @Override
    public void onLoadProductTransactionHistoryFailure() {
        listener.EnableButtonBuy();
    }

    public void LoadCurrentUser() {
        interactor.FindCurrentUser();
    }

    public void downLoadProduct(Context context, String fileName) {
        interactor.downLoadProduct(context, fileName);
    }

    public void LoadProductById(String product_id) {
        interactor.LoadProductById(product_id);
    }
}
