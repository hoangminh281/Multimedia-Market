package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductBookmark;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProductPresenter;
import com.thm.hoangminh.multimediamarket.repository.BookmarkRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.BookmarkRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.PurchasedProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.ProductView;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductPresenterImpl implements ProductPresenter {
    private boolean requestLock;
    private boolean isFirstLoading;
    private FirebaseUser currentUser;
    private final ProductView listener;
    private ArrayList<String> productIdArr;
    private ProductRepository productRepository;
    private SectionRepository sectionRepository;
    private BookmarkRepository bookmarkRepository;
    private PurchasedProductRepository purchasedProductRepository;

    public ProductPresenterImpl(ProductView listener) {
        this.listener = listener;
        productIdArr = new ArrayList<>();
        productRepository = new ProductRepositoryImpl();
        sectionRepository = new SectionRepositoryImpl();
        bookmarkRepository = new BookmarkRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        purchasedProductRepository = new PurchasedProductRepositoryImpl();
    }

    @Override
    public void extractBundle(Bundle bundle) {
        if (bundle != null) {
            String optionKey = bundle.getString(Constants.BundleOptionKey);
            if (optionKey.equals(Constants.SectionOption)) {
                String cateId = bundle.getString(Constants.CateIdKey);
                String sectionId = bundle.getString(Constants.SectionIdKey);
                String sectionTitle = bundle.getString(Constants.SectionTitle);
                listener.setTitle(sectionTitle);
                loadProductBySectionId(cateId, sectionId);
            } else if (optionKey.equals(Constants.UserOption)) {
                String userId = bundle.getString(Constants.UserIdKey);
                String cateId = bundle.getString(Constants.CateIdKey);
                String cateTitle = bundle.getString(Constants.CateTitleKey);
                listener.setTitle(cateTitle);
                loadProductByUserId(userId, cateId);
            } else if (optionKey.equals(Constants.AdminOption)) {
                String cateId = bundle.getString(Constants.CateIdKey);
                loadProductByAdmin(cateId);
            } else if (optionKey.equals(Constants.BookmarkOption)) {
                String cateId = bundle.getString(Constants.CateIdKey);
                loadProductByBookmark(cateId);
            } else if (optionKey.equals(Constants.SearchOption)) {
                String[] searchResults = bundle.getStringArray(Constants.SearchResults);
                listener.setTitle(R.string.txt_search);
                loadProductBySearchResults(searchResults);
            }
        }
    }

    private void loadProductBySectionId(String cateId, String sectionId) {
        sectionRepository.findAll(cateId, sectionId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productIdArr.clear();
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    for (DataSnapshot item : dataSnapshots) {
                        if (item.getValue(int.class) == Constants.ProductEnable)
                        productIdArr.add(item.getKey());
                    }
                    firstLoadProductPaging();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductByUserId(String userId, String cateId) {
        purchasedProductRepository.findAll(userId, cateId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productIdArr.clear();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        productIdArr.add(item.getKey());
                    }
                    firstLoadProductPaging();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductByAdmin(final String cateId) {
        productRepository.findAll(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productIdArr.clear();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        Product product = item.getValue(Product.class);
                        if (product.getCateId().equals(cateId)) {
                            productIdArr.add(product.getProductId());
                        }
                    }
                    firstLoadProductPaging();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductByBookmark(String cateId) {
        bookmarkRepository.findAll(currentUser.getUid(), cateId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productIdArr.clear();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        ProductBookmark productBookmark = item.getValue(ProductBookmark.class);
                        if (productBookmark.getValue() == Constants.BookMarkEnable) {
                            productIdArr.add(productBookmark.getProductId());
                        }
                    }
                    firstLoadProductPaging();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductBySearchResults(String[] searchResults) {
        productIdArr.clear();
        productIdArr.addAll(Arrays.asList(searchResults));
        firstLoadProductPaging();
    }

    private void firstLoadProductPaging() {
        isFirstLoading = true;
        loadProductPaging();
    }

    @Override
    public void loadProductPaging() {
        if (!requestLock && isFirstLoading) {
            requestLock = true;
            int i = 0;
            while (!productIdArr.isEmpty() && i < Constants.ProductLimitInPage) {
                String id = productIdArr.get(0);
                productIdArr.remove(0);
                productRepository.findById(id, new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            if (User.getInstance().getRole() == User.ADMIN
                                    || dataSnapshot.child(Constants.ProductStatus).getValue(int.class).equals(Constants.ProductEnable)) {
                                listener.addProductIntoAdapter(dataSnapshot.getValue(Product.class));
                                listener.refreshAdapter();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                i++;
                requestLock = false;
            }
        }
    }
}
