package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.presenter.UpdateProductPresenter;
import com.thm.hoangminh.multimediamarket.repository.FileStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.FileStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Tools;
import com.thm.hoangminh.multimediamarket.view.callback.UpdateProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateProductPresenterImpl implements UpdateProductPresenter {
    private String cateId;
    private File updatedFile;
    private String productId;
    private Bitmap productImage;
    private Product currentProduct;
    private UpdateProductView listener;
    private ArrayList<String> productSections;
    private ProductDetail currentProductDetail;
    private ProductRepository productRepository;
    private SectionRepository sectionRepository;
    private Map<String, String> sectionCategories;
    private Map<Integer, Bitmap> productDetailImages;
    private FileStorageRepository fileStorageRepository;
    private ProductDetailRepository productDetailRepository;
    private ProductStorageRepository productStorageRepository;
    private ProductDetailStorageRepository productDetailStorageRepository;


    public UpdateProductPresenterImpl(UpdateProductView listener) {
        this.listener = listener;
        productRepository = new ProductRepositoryImpl();
        sectionRepository = new SectionRepositoryImpl();
        productDetailRepository = new ProductDetailRepositoryImpl();
        productStorageRepository = new ProductStorageRepositoryImpl();
        fileStorageRepository = new FileStorageRepositoryImpl();
        productDetailStorageRepository = new ProductDetailStorageRepositoryImpl();
    }

    @Override
    public void extractBundle(Bundle bundle) {
        cateId = bundle.getString(Constants.CateIdKey);
        productId = bundle.getString(Constants.ProductIdKey);
        if (cateId.equals(Category.getInstance().get(2).getCateId())
                || cateId.equals(Category.getInstance().get(4).getCateId())) {
            listener.hideVideoEditText();
        }
        loadProduct();
    }

    private void loadProduct() {
        productRepository.findById(productId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentProduct = dataSnapshot.getValue(Product.class);
                    listener.updateProductUI(currentProduct);
                    productStorageRepository.findUriById(currentProduct.getPhotoId(), new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            listener.loadProductImageByUri(uri);
                        }

                    }, null);
                    loadProductDetail();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductDetail() {
        productDetailRepository.findById(productId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentProductDetail = dataSnapshot.getValue(ProductDetail.class);
                    listener.updateProductDetailUI(currentProductDetail, cateId);
                    loadProductSection();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadProductSection() {
        sectionRepository.findById(cateId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    Map<String, String> productSections = new HashMap<>();
                    Map<String, String> sectionCategories = new HashMap<>();
                    for (DataSnapshot item : iterable) {
                        if (sectionRepository.checkProductIdInSection(item, productId)) {
                            productSections.put(item.getKey(), item.child(Constants.SectionTitle).getValue(String.class));
                        }
                        sectionCategories.put(item.getKey(), item.child(Constants.SectionTitle).getValue(String.class));
                    }
                    updateSectionProductUI(productSections);
                    updateSectionCategoriesUI(sectionCategories);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateSectionProductUI(Map<String, String> productSections) {
        this.productSections = new ArrayList<>(productSections.keySet());
        if (productSections.size() > 0) {
            String[] stArr = productSections.values().toArray(new String[productSections.size()]);
            String productCate = stArr[0];
            for (int i = 1; i < productSections.size() - 1; i++)
                productCate += ", " + stArr[i];
            listener.showProductCateText(productCate);
        }
    }

    private void updateSectionCategoriesUI(Map<String, String> sectionCategories) {
        this.sectionCategories = sectionCategories;
        listener.setEventSectionCategories(sectionCategories);
    }

    @Override
    public void updateProduct(ArrayList<Integer> selectedProductSections, final Product product, final ProductDetail productDetail,
                              final Bitmap productImage, Map<Integer, Bitmap> productDetailBitmaps, File updatedFile) {
        String pgdMessage;
        int pgdMax;
        if (selectedProductSections.size() == 0) {
            pgdMessage = "Creating product...";
            pgdMax = 2 + (productImage != null ? 1 : 0) + productDetailBitmaps.size() + (updatedFile != null ? 1 : 0);
        } else {
            pgdMessage = "Updating sections...";
            pgdMax = 4 + (productImage != null ? 1 : 0) + productDetailBitmaps.size() + (updatedFile != null ? 1 : 0);
        }
        listener.setupProgressDialog(pgdMax, "Update product", pgdMessage);
        listener.showProgressDialog();

        this.productImage = productImage;
        this.productDetailImages = productDetailBitmaps;
        this.updatedFile = updatedFile;
        if (selectedProductSections.size() != 0) {
            deleteProductSections();
            ArrayList<String> modifiedProductSections = new ArrayList<>();
            for (int i : selectedProductSections) {
                modifiedProductSections.add((String) sectionCategories.keySet().toArray()[i]);
            }
            updateProductSections(modifiedProductSections);
        }

        currentProduct.setTitle(product.getTitle());
        currentProduct.setPrice(product.getPrice());
        productRepository.update(currentProduct,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.updateProgressDialog(R.string.dialog_successfully_update_product);
                        updateProductDetail(productDetail, currentProduct.getPhotoId(), productImage);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.dialog_failure_update_product);
                    }
                });
    }

    private void deleteProductSections() {
        for (String sectionId : productSections) {
            sectionRepository.setProductValue(cateId, sectionId, productId, Constants.SectionProductDisable,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.updateProgressDialog(R.string.dialog_successfully_delete_section);
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.showMessage(R.string.dialog_failure_delete_section_product);
                        }
                    });
        }
    }

    private void updateProductSections(ArrayList<String> selectedProductSections) {
        for (String sectionId : selectedProductSections) {
            sectionRepository.setProductValue(cateId, sectionId, productId, Constants.SectionProductEnable,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.updateProgressDialog(R.string.dialog_successfully_update_section);
                        }
                    },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.showMessage(R.string.dialog_failure_update_section_product);
                        }
                    });
        }
    }

    private void updateProductDetail(final ProductDetail productDetail, final String photoId, final Bitmap productImage) {
        currentProductDetail.setIntro(productDetail.getIntro());
        currentProductDetail.setDescription(productDetail.getDescription());
        currentProductDetail.setAgeLimit(productDetail.getAgeLimit());
        currentProductDetail.setVideoId(productDetail.getVideoId());

        productDetailRepository.update(currentProductDetail,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.updateProgressDialog(R.string.dialog_successfully_update_product_detail);
                        if (UpdateProductPresenterImpl.this.productImage != null)
                            updateProductImage(photoId, productImage);
                        if (productDetailImages.size() != 0)
                            updateProductDetailImages(currentProductDetail.getImageIdList());
                        if (updatedFile != null)
                            updateProductFile(updatedFile);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.dialog_failure_update_product);
                    }
                });
    }


    @Override
    public void loadProductDetailImages(Context context, ImageView[] imgArr) {
        if (currentProductDetail.getImageIdList() != null) {
            HashMap<String, String> imageList = currentProductDetail.getImageIdList();
            int minSize = imageList.size() < imgArr.length ? imageList.size() : imgArr.length;
            for (int i = 1; i < minSize + 1; i++) {
                listener.setTagImageView(i, Constants.HasImageTag);
                ImageLoader.loadImage(ProductDetailStorageRepository.class, context, imgArr[i], String.valueOf(imageList.values().toArray()[i - 1]));
            }
        }
    }

    private void updateProductImage(final String photoId, Bitmap productImage) {
        productStorageRepository.update(photoId, productImage,
                new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        listener.updateProgressDialog(R.string.dialog_successfully_update_image_product);
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.dialog_failure_update_image_product);

                    }
                });
    }

    private void updateProductDetailImages(HashMap<String, String> imageList) {
        if (imageList == null) imageList = new HashMap<>();
        Integer[] newBitmapIndexs = productDetailImages.keySet().toArray(new Integer[productDetailImages.size()]);
        final Bitmap[] newBitmaps = productDetailImages.values().toArray(new Bitmap[productDetailImages.size()]);
        final String[] oldImageKeys = imageList.keySet().toArray(new String[imageList.size()]);
        String[] oldImageValues = imageList.values().toArray(new String[imageList.size()]);
        //Remove image old by user
        for (int i = 0; i < imageList.size(); i++) {
            boolean removed = true;
            for (int newBitmapIndex : newBitmapIndexs) {
                if (i == newBitmapIndex) {
                    removed = false;
                    break;
                }
            }
            if (removed) {
                final int finalI = i;
                productDetailStorageRepository.remove(oldImageValues[i], new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        productDetailRepository.removeImageId(productId, oldImageKeys[finalI], null, null);
                    }
                }, null);
            }
        }
        String newImageId = null;
        String tempId;
        //Update image old with new
        for (int i = 0; i < productDetailImages.size(); i++) {
            final int finalI = i;
            final int index = newBitmapIndexs[i];
            if (index < imageList.size()) {
                productDetailStorageRepository.update(oldImageValues[index], newBitmaps[finalI],
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                listener.updateProgressDialog(R.string.dialog_successfully_update_image_product_detail);
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.showMessage(R.string.dialog_failure_update_image_product_detail);

                            }
                        });
            } else {
                do {
                    tempId = Tools.createRandomImageName();
                } while (newImageId == tempId);
                newImageId = tempId;
                final String finalNewImageId = tempId;
                productDetailRepository.pushImageId(productId, newImageId,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                productDetailStorageRepository.add(finalNewImageId, newBitmaps[finalI],
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                listener.updateProgressDialog(R.string.dialog_successfully_update_image_product_detail);
                                            }
                                        }, new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                listener.showMessage(R.string.dialog_failure_update_image_product_detail);

                                            }
                                        });
                            }
                        }, null);
            }
        }

    }

    private void updateProductFile(final File updatedFile) {
        final String fileId = Tools.createRandomFileName(updatedFile.getName());
        updatedFile.setName(fileId);
        fileStorageRepository.updateOrCreate(currentProductDetail.getFileId(), updatedFile,
                new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        productDetailRepository.setFileId(productId, fileId,
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        productDetailRepository.setCapacity(productId, updatedFile.getSize().getValue(),
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        listener.updateProgressDialog(R.string.dialog_successfully_upload_file);
                                                    }
                                                },
                                                new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        listener.showMessage(R.string.dialog_failure_delete_file);
                                                    }
                                                });
                                    }
                                },
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.showMessage(R.string.dialog_failure_delete_file);
                                    }
                                });
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.dialog_failure_delete_file);
                    }
                });
    }
}
