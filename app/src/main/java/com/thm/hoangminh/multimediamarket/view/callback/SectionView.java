package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Section;

import java.util.ArrayList;

public interface SectionView {
    public void addSectionCardview(ArrayList<Section> sectionArr);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();
}
