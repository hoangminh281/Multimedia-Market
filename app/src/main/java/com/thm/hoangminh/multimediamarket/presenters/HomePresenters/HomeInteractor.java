package com.thm.hoangminh.multimediamarket.presenters.HomePresenters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeInteractor {
    HomeListener listener;
    DatabaseReference mRef;

    public HomeInteractor(HomeListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void LoadGamesBySectionPaging(final SectionDataModel sectionDataModel, int game_count) {
        int i = 0;
        while (!sectionDataModel.getGame_id_arr().isEmpty() && i < game_count) {
            String game_id = sectionDataModel.getGame_id_arr().get(0);
            sectionDataModel.getGame_id_arr().remove(0);
            mRef.child("games/" + game_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        sectionDataModel.addItemInSection(dataSnapshot.getValue(Game.class));
                        listener.onLoadGameBySectionSuccess(sectionDataModel);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            i++;
        }
    }


    public void LoadHomeMenuPaging(String begin_id, int count, final int game_limit) {
        if (begin_id != null)
            mRef.child("sections").orderByKey().startAt(begin_id).limitToFirst(count).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<Section> sectionArr = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        Section section = item.getValue(Section.class);
                        sectionArr.add(section);
                    }
                    listener.onLoadSectionSuccess(sectionArr);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        else {
            mRef.child("sections").orderByKey().limitToFirst(count).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<Section> sectionArr = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        Section section = item.getValue(Section.class);
                        sectionArr.add(section);
                    }
                    listener.onLoadSectionSuccess(sectionArr);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
