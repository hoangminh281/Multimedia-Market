package com.thm.hoangminh.multimediamarket.presenter.service;

import com.thm.hoangminh.multimediamarket.models.SectionDataModel;

public interface SectionPresenter {
    void reset();

    void LoadSectionWithPaging(String keyMode);

    void LoadProductBySectionWithPaging(SectionDataModel sdm, boolean enableRefreshSectionListDataAdapter);
}
