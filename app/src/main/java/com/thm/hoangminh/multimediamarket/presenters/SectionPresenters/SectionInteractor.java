package com.thm.hoangminh.multimediamarket.presenters.SectionPresenters;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SectionInteractor {
    private SectionListener listener;
    private DatabaseReference mRef;

    public SectionInteractor(SectionListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public synchronized void LoadProductsBySectionPaging(final SectionDataModel sectionDataModel, int product_count) {
        int i = 0;
        while (!sectionDataModel.getProduct_id_arr().isEmpty() && i < product_count) {
            String product_id = sectionDataModel.getProduct_id_arr().get(0);
            sectionDataModel.getProduct_id_arr().remove(0);
            mRef.child("products/" + product_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (User.getInstance().getRole() == User.ADMIN || dataSnapshot.child("status").getValue(int.class).equals(1))
                            sectionDataModel.addItemInSection(dataSnapshot.getValue(Product.class));
                        listener.onLoadProductBySectionSuccess(sectionDataModel);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            i++;
        }
    }

    public void LoadSectionPaging(final String keyMode, String begin_id, int count) {
        Query mQuery;
        if (begin_id != null)
            mQuery = mRef.child("sections/" + keyMode).orderByKey().startAt(begin_id).limitToFirst(count);
        else {
            mQuery = mRef.child("sections/" + keyMode).orderByKey().limitToFirst(count);
        }
        if (mQuery != null) {
            mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                        ArrayList<Section> sectionArr = new ArrayList<>();
                        for (DataSnapshot item : iterable) {
                            Section section = item.getValue(Section.class);
                            section.setSection_id(section.getSection_id());
                            if (section.getProduct_id() != null) {
                                Iterator<Map.Entry<String, Integer>> ite = section.getProduct_id().entrySet().iterator();
                                while (ite.hasNext()) {
                                    Map.Entry<String, Integer> productId = ite.next();
                                    if (productId.getValue() == 0)
                                        ite.remove();
                                }
                            }
                            sectionArr.add(section);
                        }
                        listener.onLoadSectionSuccess(sectionArr);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
