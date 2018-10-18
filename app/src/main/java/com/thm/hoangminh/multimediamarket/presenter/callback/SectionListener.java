package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.model.SectionDataModel;

import java.util.ArrayList;

public interface SectionListener {
    public void onLoadSectionSuccess(ArrayList<Section> sectionArr);

    public void onLoadProductBySectionSuccess(SectionDataModel sectionDataModel);
}
