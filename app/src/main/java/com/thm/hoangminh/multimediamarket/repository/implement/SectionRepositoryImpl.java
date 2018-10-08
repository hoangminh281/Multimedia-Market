package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;
import com.thm.hoangminh.multimediamarket.models.Pageable;
import com.thm.hoangminh.multimediamarket.models.Section;

import java.util.ArrayList;

public class SectionRepositoryImpl implements SectionRepository<Section, String, ValueEventListener> {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void findAll(String sectionId, Pageable pageable, ValueEventListener event) {
        Query mQuery = mRef.child(ROUTE.SECTION(sectionId)).orderByKey();
        if (pageable.getFirstId() != null)
            mQuery = mQuery.startAt(pageable.getFirstId());
        if (pageable.getCount() != 0)
            mQuery = mQuery.limitToFirst(pageable.getCount());
        if (mQuery != null) {
            mQuery.addListenerForSingleValueEvent(event);
        }
    }

    @Override
    public void add(Section item) {

    }

    @Override
    public void update(Section item) {

    }

    @Override
    public void remove(Section item) {

    }

    @Override
    public ArrayList<Section> findAll(ValueEventListener event) {
        return null;
    }

    @Override
    public void findById(String s, ValueEventListener event) {

    }
}
