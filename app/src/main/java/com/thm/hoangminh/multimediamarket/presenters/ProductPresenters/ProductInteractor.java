package com.thm.hoangminh.multimediamarket.presenters.ProductPresenters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;
import java.util.List;

public class ProductInteractor {
    private ProductListener listener;
    private DatabaseReference mRef;
    private FirebaseUser user;

    public ProductInteractor(ProductListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void LoadProductBySection(String cate_id, String section_id) {
        mRef.child("sections/" + cate_id + "/" + section_id + "/product_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                    ArrayList<String> productIds = new ArrayList<>();
                    for (DataSnapshot item : dataSnapshots) {
                        productIds.add(item.getKey());
                    }
                    listener.onLoadProductIdListSuccess(productIds);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadProductPaging(List<String> product_Id_List, int item_count) {
        int i = 0;
        while (!product_Id_List.isEmpty() && i < item_count) {
            String id = product_Id_List.get(0);
            product_Id_List.remove(0);
            mRef.child("products/" + id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (User.getInstance().getRole() == User.ADMIN || dataSnapshot.child("status").getValue(int.class).equals(1))
                            listener.onLoadProductPagingSuccess(dataSnapshot.getValue(Product.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            i++;
        }
    }

    public void LoadProductByUser(String user_id, String cate_id) {
        mRef.child("purchased_product/" + user_id + "/" + cate_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<String> productIDList = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        productIDList.add(item.getKey());
                    }
                    listener.onLoadProductIdListSuccess(productIDList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadProductByBookmarkCateIdPaging(String bookmark_cate_id) {
        mRef.child("bookmark/" + user.getUid() + "/" + bookmark_cate_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                ArrayList<String> productList = new ArrayList<>();
                for (DataSnapshot item : iterable) {
                    if (item.getValue(int.class) == 1) {
                        productList.add(item.getKey());
                    }
                }
                listener.onLoadProductIdListSuccess(productList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadProductByAdmin(final String productAdminKey) {
        mRef.child("products/").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> productIds = new ArrayList<>();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        if (item.child("cate_id").getValue(String.class).equals(productAdminKey))
                            productIds.add(item.getKey());
                    }
                    listener.onLoadProductIdListSuccess(productIds);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
