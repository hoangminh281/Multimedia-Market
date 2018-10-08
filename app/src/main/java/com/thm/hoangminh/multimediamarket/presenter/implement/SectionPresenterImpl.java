package com.thm.hoangminh.multimediamarket.presenter.service.implement;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.config.constant.Constants;
import com.thm.hoangminh.multimediamarket.models.Pageable;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenter.service.SectionPresenter;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.SectionView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SectionPresenterImpl implements SectionPresenter {
    private Pageable pageable;
    private boolean requestBlock;
    private SectionView listener;
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
    public void reset() {
        pageable.setFirstId(null);
        requestBlock = false;
    }

    @Override
    public void LoadSectionWithPaging(String keyMode) {
        if (!requestBlock) {
            requestBlock = true;
            sectionRepository.findAll(keyMode, this.pageable, new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) { //Lấy danh sách section có chứa danh sách id sản phẩm
                            Section section = iterator.next().getValue(Section.class);
                            if (!iterator.hasNext()) {
                                String lastSectionId = section.getSectionId();
                                pageable.setFirstId(lastSectionId);
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
                                    section.setProductIdArr(limitedProductIdArr);
                                    listener.addSectionCardview(section);
                                }
                            }
                        }
                    }
                    requestBlock = false;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    requestBlock = false;
                }

            });
        }
    }

    @Override
    public void LoadProductBySectionWithPaging(final SectionDataModel sectionDataModel, final boolean enableRefreshSectionListDataAdapter) {
        if (!sectionDataModel.isRequestBlock()) {
            sectionDataModel.setRequestBlock(true);
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
                                    sectionDataModel.addItemIntoSection(dataSnapshot.getValue(Product.class));
                                }
                                if (enableRefreshSectionListDataAdapter)
                                    listener.refreshSectionListDataAdapter();
                                else listener.refreshAdapter();
                                sectionDataModel.setRequestBlock(false);
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
