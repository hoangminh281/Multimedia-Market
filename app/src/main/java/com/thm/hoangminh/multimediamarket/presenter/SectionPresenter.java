package com.thm.hoangminh.multimediamarket.presenter;

import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.model.SectionDataModel;

public interface SectionPresenter {
    void extractBundle(Bundle bundle);

    void reset();

    void loadSectionWithPaging();

    void loadProductBySectionWithPaging(SectionDataModel sectionDataModel);
}
