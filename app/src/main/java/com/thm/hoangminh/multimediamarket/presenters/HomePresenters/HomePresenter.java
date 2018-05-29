package com.thm.hoangminh.multimediamarket.presenters.HomePresenters;

import android.util.Log;

import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;
import com.thm.hoangminh.multimediamarket.views.HomeViews.HomeView;

import java.util.ArrayList;

public class HomePresenter implements HomeListener {
    private HomeInteractor interactor;
    private HomeView listener;
    private String begin_id;
    private int section_count;
    private int game_count;
    private boolean section_deny;
    private int game_limit;

    public HomePresenter(HomeView listener) {
        this.listener = listener;
        this.interactor = new HomeInteractor(this);
        this.section_count = 6; //only get 5 sections, the last item will get after
        this.game_count = 5; //get 5 games in a section once time
        this.game_limit = 15; //show only 15 games in a section
    }

    public void LoadHomeSectionPaging() {
        if (!section_deny) {
            //homeView.showBottomProgressbar();
            section_deny = true;
            interactor.LoadHomeMenuPaging(begin_id, section_count, game_limit);
        }
    }

    public void LoadGamesBySectionPaging(SectionDataModel sectionDataModel) {
        if (!sectionDataModel.isRequest_deny()) {
            sectionDataModel.setRequest_deny(true);
            if (sectionDataModel.getGame_id_arr() != null) {
                int size = sectionDataModel.getGame_id_arr().size();
                sectionDataModel.setGame_id_arr(sectionDataModel.getGame_id_arr().subList(0, game_limit > size ? size : game_limit));
                interactor.LoadGamesBySectionPaging(sectionDataModel, game_count);
            }
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

    @Override
    public void onLoadGameBySectionSuccess(SectionDataModel sectionDataModel) {
        listener.refreshAdapter();
        sectionDataModel.setRequest_deny(false);
    }
}
