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
import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
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
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;
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
        productId = bundle.getString(Constants.ProductIdKey);
        cateId = bundle.getString(Constants.CateIdKey);

        if (cateId.contains(MainActivity.categories.get(1).getCateId())
                || cateId.contains(MainActivity.categories.get(4).getCateId())) {
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
                    listener.UpdateProductUI(currentProduct);
                    productStorageRepository.findDownloadUriById(currentProduct.getPhotoId(), new OnSuccessListener<Uri>() {
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
                    listener.UpdateProductDetailUI(currentProductDetail, cateId);
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
                        if (sectionRepository.checkProductByDataSnapshotAndProductId(item, productId)) {
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

    private void updateSectionCategoriesUI(Map<String, String> sectionCategories) {
        this.sectionCategories = sectionCategories;
        listener.setEventSectionCategories(sectionCategories);
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
                        listener.UpdateProgressDialog(R.string.dialog_successfully_update_product);
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

    private void updateProductDetail(final ProductDetail productDetail, final String photoId, final Bitmap productImage) {
        currentProductDetail.setIntro(productDetail.getIntro());
        currentProductDetail.setDescription(productDetail.getDescription());
        currentProductDetail.setAgeLimit(productDetail.getAgeLimit());
        currentProductDetail.setVideo(productDetail.getVideo());

        productDetailRepository.update(currentProductDetail,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.UpdateProgressDialog(R.string.dialog_successfully_update_product_detail);
                        if (UpdateProductPresenterImpl.this.productImage != null)
                            updateProductImage(photoId, productImage);
                        if (productDetailImages.size() != 0)
                            updateProductDetailImages(currentProductDetail.getImageList());
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
        if (currentProductDetail.getImageList() != null) {
            HashMap<String, String> imageList = currentProductDetail.getImageList();
            int minSize = imageList.size() < imgArr.length ? imageList.size() : imgArr.length;
            for (int i = 1; i < minSize + 1; i++) {
                ImageLoader.loadImageProduct(productDetailStorageRepository, context, imgArr[i], String.valueOf(imageList.values().toArray()[i - 1]));
                listener.setTagImageView(i, Constants.HasImageTag);
            }
        }
    }

    private void updateProductImage(final String photoId, Bitmap productImage) {
        productStorageRepository.update(photoId, productImage,
                new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        listener.UpdateProgressDialog(R.string.dialog_successfully_update_image_product);
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
                                listener.UpdateProgressDialog(R.string.dialog_successfully_update_image_product_detail);
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
                    tempId = Tools.createImageNameRandom();
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
                                                listener.UpdateProgressDialog(R.string.dialog_successfully_update_image_product_detail);
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
        final String fileId = Tools.createFileNameRandom(updatedFile.getName());
        updatedFile.setName(fileId);
        fileStorageRepository.update(currentProductDetail.getDownloadLink(), updatedFile,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        productDetailRepository.setFileId(productId, fileId,
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        productDetailRepository.setCapacity(productId, updatedFile.getSize().getValue(),
                                                new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        listener.UpdateProgressDialog(R.string.dialog_successfully_upload_file);
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

    private void updateProductSections(ArrayList<String> selectedProductSections) {
        for (String sectionId : selectedProductSections) {
            sectionRepository.setProductValueByProductId(cateId, sectionId, productId, Constants.SectionProductEnable,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.UpdateProgressDialog(R.string.dialog_successfully_update_section);
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

    private void deleteProductSections() {
        for (String sectionId : productSections) {
            sectionRepository.setProductValueByProductId(cateId, sectionId, productId, Constants.SectionProductDisable,
                    new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.UpdateProgressDialog(R.string.dialog_successfully_delete_section);
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.showMessage(R.string.dialog_failure_delete_section_product);
                        }
                    });
        }
    }
}
