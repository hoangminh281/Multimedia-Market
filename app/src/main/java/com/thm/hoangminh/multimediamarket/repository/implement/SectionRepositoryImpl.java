package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Pageable;
import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.utility.Validate;

import java.util.Map;

public class SectionRepositoryImpl implements SectionRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(Section item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(Section item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(Section item, OnSuccessListener successListener, OnFailureListener failureListener) {

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
    public void findAll(String cateId, String sectionId, ValueEventListener eventListener) {
        mRef.child(ROUTE.SECTION(cateId, sectionId)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public boolean checkProductIdInSection(DataSnapshot dataSnapshot, String productId) {
        boolean validate = dataSnapshot.child(ROUTE.SECTION_PRODUCTIDARR(productId)).exists()
                && dataSnapshot.child(ROUTE.SECTION_PRODUCTIDARR(productId)).getValue(int.class) == Constants.SectionProductEnable;
        return validate;
    }

    @Override
    public void findAll(ValueEventListener event) {
    }

    @Override
    public void findById(String cateId, ValueEventListener eventListener) {
        mRef.child(ROUTE.SECTION(cateId)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void setProductValue(String cateId, String sectionId, String productId, int value, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.SECTION(cateId, sectionId, productId)).setValue(value)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void removeProductIdArr(String cateId, String sectionId, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.SECTION(cateId, sectionId)).removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setProductValueList(String cateId, String sectionId, Map<String, Integer> productIds, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.SECTION(cateId, sectionId)).setValue(productIds)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
