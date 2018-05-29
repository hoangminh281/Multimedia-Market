package com.thm.hoangminh.multimediamarket.views.HomeViews;

import android.view.View;

import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.models.Section;

import java.util.ArrayList;

public interface HomeView {
    public void addSectionCardview(ArrayList<Section> sectionArr);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();
}
