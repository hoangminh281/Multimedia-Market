package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.Pageable;
import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;

public class SectionRepositoryImpl implements SectionRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(Section item, ValueEventListener eventListener) {

    }

    @Override
    public void update(Section item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(Section item, ValueEventListener eventListener) {
    
    }

    @Override
    public void findByPageable(String cateId, Pageable pageable, ValueEventListener event) {
        Query mQuery = mRef.child(ROUTE.SECTION(cateId)).orderByKey();
        if (pageable.getFirstId() != null) {
            mQuery = mQuery.startAt(pageable.getFirstId());
        }
        if (pageable.getLimit() != 0)
            mQuery = mQuery.limitToFirst(pageable.getLimit());
        if (mQuery != null) {
            mQuery.addListenerForSingleValueEvent(event);
        }
    }

    @Override
    public void findAll(ValueEventListener event) {
    }

    @Override
    public void findById(String s, ValueEventListener event) {

    }
}
