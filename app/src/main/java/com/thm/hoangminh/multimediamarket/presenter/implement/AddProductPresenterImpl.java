package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;
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
import com.thm.hoangminh.multimediamarket.utility.Tools;
import com.thm.hoangminh.multimediamarket.repository.FileStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.FileStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.ModifyProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProductPresenterImpl implements AddProductPresenter {
    private String cateId;
    private ModifyProductView listener;
    private FirebaseUser currentUser;
    private UserRepository userRepository;
    private Map<String, String> sectionList;
    private ValueEventListener eventListener;
    private SectionRepository sectionRepository;
    private ProductRepository productRepository;
    private FileStorageRepository fileStorageRepository;
    private ProductDetailRepository productDetailRepository;
    private ProductDetailStorageRepository productDetailStorageRepository;

    public AddProductPresenterImpl(ModifyProductView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        sectionRepository = new SectionRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        fileStorageRepository = new FileStorageRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        productDetailRepository = new ProductDetailRepositoryImpl();
        productDetailStorageRepository = new ProductDetailStorageRepositoryImpl();
    }

    @Override
    public void extractBundle(Context context, Bundle bundle) {
        String cateId = bundle.getString(Constants.CateIdKey);
        this.cateId = cateId;
        bindingCurrentUserRole(context);
        loadSectionProduct(cateId);
        listener.setCateId(cateId);
    }

    private void bindingCurrentUserRole(final Context context) {
        eventListener = userRepository.findAndWatchRole(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Validate.validateCurrentUserRole(context, dataSnapshot.getValue(int.class),
                            new int[]{Constants.AdminRole, Constants.ModRole});
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadSectionProduct(String cateId) {
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
        DatabaseReference mRef = productRepository.createDataRef(null);
        final String productId = mRef.getKey();
        product.setProductId(productId);
        final String avatarId = Tools.createRandomImageName();
        product.setCateId(cateId);
        product.setPhotoId(avatarId);
        product.setRating(Constants.RatingMaxPoint);
        product.setStatus(Constants.ProductEnable);

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
                addProductDetail(productDetail, preparedSelectedImages, avatarId);
                addFile(pickedFile);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.updateProgressMessage("Created failure product");
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
                            listener.updateProgressMessage("Updated failure section " + sectionTitle);
                        }
                    });
        }
    }

    private void addProductDetail(final ProductDetail productDetail, final Map<String, Bitmap> preparedSelectedImages, final String avatarId) {
        productDetailRepository.add(productDetail, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                listener.updateProgressMessage("Created successfully product detail");
                addProductDetailImages(productDetail.getId(), preparedSelectedImages, avatarId);
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.updateProgressMessage("Created failure product detail");
            }
        });
    }

    private void addProductDetailImages(String productId, Map<String, Bitmap> preparedSelectedImages, String avatarId) {
        //set and upload product detail images
        for (final Map.Entry<String, Bitmap> entry : preparedSelectedImages.entrySet()) {
            if (entry.getKey().equals(avatarId)) {
                //upload avatar
                productDetailStorageRepository.add(avatarId, entry.getValue(), new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        listener.updateProgressMessage("Uploaded successfully product avatar");
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.updateProgressMessage("Uploaded failure product avatar");
                    }
                });
            } else {
                productDetailRepository.pushImageId(productId, entry.getKey(), new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        final int[] i = {1};
                        productDetailStorageRepository.add(entry.getKey(), entry.getValue(), new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                listener.updateProgressMessage("Uploaded successfully product detail image " + i[0]++);
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.updateProgressMessage("Uploaded failure product detail image " + i[0]++);
                            }
                        });
                    }
                }, null);
            }
        }
    }

    private void addFile(File pickedFile) {
        fileStorageRepository.add(pickedFile.getName(), pickedFile, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                listener.updateProgressMessage("Uploaded successfully file");
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.updateProgressMessage("Uploaded failure file");
            }
        });
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
    public void removeEventListener() {
        if (eventListener != null) {
            userRepository.removeFindAndWatchRoleListener(currentUser.getUid(), eventListener);
        }
    }
}
