package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.ProductBookmark;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface BookmarkRepository extends Repository<ProductBookmark, String> {
    void findByProductBookmark(ProductBookmark productBookmark, ValueEventListener eventListener);
}
