package com.thm.hoangminh.multimediamarket.presenters.SectionPresenters;

import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;

import java.util.ArrayList;

public interface SectionListener {
    public void onLoadSectionSuccess(ArrayList<Section> sectionArr);

    public void onLoadProductBySectionSuccess(SectionDataModel sectionDataModel);
}
