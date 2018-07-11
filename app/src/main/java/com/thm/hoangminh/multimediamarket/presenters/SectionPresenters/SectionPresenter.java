package com.thm.hoangminh.multimediamarket.presenters.SectionPresenters;

import android.util.Log;

import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;
import com.thm.hoangminh.multimediamarket.views.SectionViews.SectionView;

import java.util.ArrayList;
import java.util.List;

public class SectionPresenter implements SectionListener {
    private SectionInteractor interactor;
    private SectionView listener;
    private String begin_id;
    private int section_count;
    private int product_count;
    private boolean section_deny;
    private int product_limit;

    public SectionPresenter(SectionView listener) {
        this.listener = listener;
        this.interactor = new SectionInteractor(this);
        this.section_count = 6; //only get 5 sections, the last item will get after
        this.product_count = 5; //loading 5 products once a section
        this.product_limit = 15; //loading only 15 products maximum in a section
    }

    public void LoadSectionPaging(String keyMode) {
        if (!section_deny) {
            //homeView.showBottomProgressbar();
            section_deny = true;
            interactor.LoadSectionPaging(keyMode, begin_id, section_count);
        }
    }

    @Override
    public void onLoadSectionSuccess(ArrayList<Section> sectionArr) {
        if (sectionArr.size() == section_count) {
            begin_id = sectionArr.get(sectionArr.size() - 1).getSection_id();
            sectionArr.remove(section_count - 1);
            section_deny = false;
        }
        //homeView.hideBottomProgressbar();
        listener.addSectionCardview(sectionArr);
    }

    public void LoadProductsBySectionPaging(SectionDataModel sectionDataModel) {
        if (!sectionDataModel.isRequest_deny()) {
            sectionDataModel.setRequest_deny(true);
            if (sectionDataModel.getProduct_id_arr() != null) {
                int size = sectionDataModel.getProduct_id_arr().size();
                sectionDataModel.setProduct_id_arr(sectionDataModel.getProduct_id_arr().subList(0, product_limit > size ? size : product_limit));
                interactor.LoadProductsBySectionPaging(sectionDataModel, product_count);
            }
        }
    }

    @Override
    public void onLoadProductBySectionSuccess(SectionDataModel sectionDataModel) {
        listener.refreshAdapter();
        sectionDataModel.setRequest_deny(false);
    }
}
