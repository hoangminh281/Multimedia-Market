package com.thm.hoangminh.multimediamarket.view.SectionViews;

import com.thm.hoangminh.multimediamarket.models.Section;

import java.util.ArrayList;

public interface SectionView {
    public void addSectionCardview(ArrayList<Section> sectionArr);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();
}
