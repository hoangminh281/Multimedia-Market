package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Pageable;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.model.SectionDataModel;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.SectionPresenter;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.SectionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SectionPresenterImpl implements SectionPresenter {
    private String cateId;
    private Pageable pageable;
    private SectionView listener;
    private boolean requestBlock;
    private final int extraOneFetch = 1;
    private SectionRepository sectionRepository;
    private ProductRepository productRepository;

    public SectionPresenterImpl(SectionView listener) {
        this.listener = listener;
        sectionRepository = new SectionRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        pageable = new Pageable(Constants.SectionLimit + extraOneFetch);
    }

    @Override
    public void extractBundle(Bundle bundle) {
        cateId = bundle.getString(Constants.BundleKey);
        loadSectionWithPaging();
    }

    @Override
    public void reset() {
        pageable.setFirstId(null);
        requestBlock = false;
    }

    @Override
    public void loadSectionWithPaging() {
        if (!requestBlock) {
            requestBlock = true;
            sectionRepository.findByPageable(cateId, this.pageable, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        int count = 0;
                        while (iterator.hasNext()) { //Lấy danh sách section có chứa danh sách id sản phẩm
                            count++;
                            Section section = iterator.next().getValue(Section.class);
                            if (!iterator.hasNext() && count == pageable.getLimit()) {
                                String lastSectionId = section.getSectionId();
                                pageable.setFirstId(lastSectionId);
                                requestBlock = false;
                                break;
                            }
                            if (section.getProductIdArr() != null) {
                                Iterator<Map.Entry<String, Integer>> ite = section.getProductIdArr().entrySet().iterator();
                                HashMap<String, Integer> limitedProductIdArr = new HashMap<>();
                                while (ite.hasNext()) {
                                    if (limitedProductIdArr.size() == Constants.entireProductLimitInSection)
                                        break;
                                    Map.Entry<String, Integer> productIdAndValue = ite.next();
                                    if (productIdAndValue.getValue() == Constants.ProductEnable)
                                        limitedProductIdArr.put(productIdAndValue.getKey(), productIdAndValue.getValue());
                                }
                                if (limitedProductIdArr.size() != 0) {
                                    SectionDataModel dataModel = new SectionDataModel();
                                    dataModel.setCateId(cateId);
                                    dataModel.setSectionId(section.getSectionId());
                                    dataModel.setHeaderTitle(section.getTitle());
                                    if (limitedProductIdArr != null) {
                                        dataModel.setProductIdArr(new ArrayList<>(limitedProductIdArr.keySet()));
                                    }
                                    listener.addSectionDataModelToCardview(dataModel);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    requestBlock = false;
                }

            });
        }
    }

    @Override
    public void loadProductBySectionWithPaging(final SectionDataModel sectionDataModel) {
        if (!sectionDataModel.isRequestLock()) {
            sectionDataModel.setRequestLock(true);
            if (sectionDataModel.getProductIdArr() != null) {
                for (int i = 0; i < Constants.ProductLimit && !sectionDataModel.getProductIdArr().isEmpty(); i++) {
                    String productId = sectionDataModel.getProductIdArr().get(0);
                    sectionDataModel.getProductIdArr().remove(0);
                    productRepository.findById(productId, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                if (User.getInstance().getRole() == User.ADMIN || dataSnapshot.child(Constants.ProductStatus)
                                        .getValue(int.class).equals(Constants.ProductEnable)) {
                                    sectionDataModel.addItemInSection(dataSnapshot.getValue(Product.class));
                                }
                                listener.refreshAdapter();
                                sectionDataModel.setRequestLock(false);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        }
    }
}
