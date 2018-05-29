package com.thm.hoangminh.multimediamarket.presenters.HomePresenters;

import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;

import java.util.ArrayList;

public interface HomeListener {
    public void onLoadSectionSuccess(ArrayList<Section> sectionArr);

    public void onLoadGameBySectionSuccess(SectionDataModel sectionDataModel);
}
