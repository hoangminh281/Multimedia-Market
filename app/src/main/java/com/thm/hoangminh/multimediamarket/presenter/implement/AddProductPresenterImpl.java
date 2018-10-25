package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.presenter.AddProductPresenter;
import com.thm.hoangminh.multimediamarket.presenter.ModifyProductPresenters.ModifyProductInteractor;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.ModifyProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProductPresenterImpl implements AddProductPresenter {
    private String cateId;
    private ModifyProductView listener;
    private ModifyProductInteractor interactor;
    private FirebaseUser currentUser;
    private Map<String, String> sectionList;
    private SectionRepository sectionRepository;
    private ProductRepository productRepository;
    private Map<String, String> currentProductSections;
    private ProductDetailRepository productDetailRepository;

    public AddProductPresenterImpl(ModifyProductView listener) {
        this.listener = listener;
        sectionRepository = new SectionRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productDetailRepository = new ProductDetailRepositoryImpl();
        interactor = new ModifyProductInteractor(this);
    }

    @Override
    public void extractBundle(Bundle bundle) {
        String cateId = bundle.getString(Constants.CateIdKey);
        this.cateId = cateId;
        bindingCurrentUserRole();
        loadCategoryProduct(cateId);
    }

    private void bindingCurrentUserRole() {
        //if role == user then out
    }

    private void loadCategoryProduct(String cateId) {
        if (cateId.equals(Category.getInstance().get(2).getCateId()) || cateId.equals(Category.getInstance().get(4).getCateId())) {
            listener.hideEdtYoutube();
        }
        sectionRepository.findById(cateId, new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> sections = new HashMap<>();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        Section section = item.getValue(Section.class);
                        sections.put(section.getSectionId(), section.getTitle());
                    }
                    sectionList = sections;
                    listener.showSectionList(sections);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addProduct(Product product, final ProductDetail productDetail, final ArrayList<Integer> selectedSections, final ArrayList<Bitmap> selectedBitmaps, final File pickedFile) {
        DatabaseReference mRef = productRepository.createDataRef();
        final String productId = mRef.getKey();
        product.setProductId(productId);
        String avatarId = Tools.createRandomImageName();
        product.setPhotoId(avatarId);

        productDetail.setId(productId);
        productDetail.setOwnerId(currentUser.getUid());
        String fileId = Tools.createRandomFileName(pickedFile.getName());
        productDetail.setFileId(fileId);
        pickedFile.setName(fileId);

        final Map<String, Bitmap> preparedSelectedImages = prepareImages(avatarId, selectedBitmaps);
        productRepository.addByDataRef(mRef, product, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.updateProgressMessage("Created successfully product");
                addProductSection(productId, selectedSections);
                addProductDetail(productDetail, preparedSelectedImages);
                addFile(pickedFile);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage("Created failure product");
            }
        });
    }

    private void addProductSection(String productId, ArrayList<Integer> selectedSections) {
        for (int i : selectedSections) {
            String sectionId = sectionList.keySet().toArray()[i].toString();
            final String sectionTitle = sectionList.values().toArray()[i].toString();
            sectionRepository.setProductValue(cateId, sectionId, productId, Constants.ProductEnable,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.updateProgressMessage("Updated successfully section " + sectionTitle);
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    private void addProductDetail(ProductDetail productDetail, final Map<String, Bitmap> preparedSelectedImages) {
        productDetailRepository.add(productDetail, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                addProductDetailImages(preparedSelectedImages);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage("Created failure product detail");
            }
        });
    }

    private void addProductDetailImages(Map<String,Bitmap> preparedSelectedImages) {

    }

    private void addFile(File pickedFile) {
    }

    private Map<String, Bitmap> prepareImages(String avatarId, ArrayList<Bitmap> selectedBitmaps) {
        Map<String, Bitmap> preparedSelectedImages = new HashMap<>();
        preparedSelectedImages.put(avatarId, selectedBitmaps.get(0));
        for (int i = 1; i < selectedBitmaps.size(); i++) {
            String imageId;
            do {
                imageId = Tools.createRandomImageName();
            } while (imageId != null && preparedSelectedImages.keySet().contains(imageId));
            preparedSelectedImages.put(imageId, selectedBitmaps.get(i));
        }
        return preparedSelectedImages;
    }

    @Override
    public void onCreateNewProductFailure() {
        listener.showMessage("Created failure product");
    }

    @Override
    public void onUploadPhotoSucceed(int photoIndex) {
        listener.updateProgressMessage("Created successfully product detail");
        interactor.UploadPhoto(photoIndex, photoNames.get(photoIndex), bitmaps.get(photoIndex));
    }

    @Override
    public void onUploadPhotoSuccess(int photoIndex) {
        listener.updateProgressMessage("Uploaded successfully image " + (photoIndex + 1));
    }

    @Override
    public void onUploadPhotoFailure(int photoIndex) {
        listener.showMessage("Upload failure image " + (photoIndex + 1));
    }

    @Override
    public void onUploadFileSuccess() {
        listener.updateProgressMessage("Uploaded successfully file " + fileName);
    }

    @Override
    public void onUpdateSectionSuccess(String section) {
        listener.updateProgressMessage("Updated successfully section " + section);
    }
}
