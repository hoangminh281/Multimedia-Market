package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Pageable;
import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface SectionRepository extends Repository<Section, String> {
    void findByPageable(String cateId, Pageable pageable, ValueEventListener event);

    void findAll(String cateId, String sectionId, ValueEventListener eventListener);

    boolean checkProductIdInSection(DataSnapshot dataSnapshot, String productId);

    void setProductValue(String cateId, String sectionId, String productId, int value, OnSuccessListener<Void> successListener, OnFailureListener failureListener);
}
