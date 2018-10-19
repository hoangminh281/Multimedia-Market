package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.SectionDataModel;

public interface SectionView {
    void refreshAdapter();

    void addSectionDataModelToCardview(SectionDataModel sectionDataModel);
}
