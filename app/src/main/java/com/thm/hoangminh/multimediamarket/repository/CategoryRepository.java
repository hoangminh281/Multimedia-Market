package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface CategoryRepository extends Repository<Category, String, ValueEventListener> {
}
