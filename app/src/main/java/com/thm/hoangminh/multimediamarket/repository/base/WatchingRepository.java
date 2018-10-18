package com.thm.hoangminh.multimediamarket.repository.base;

import com.google.firebase.database.ValueEventListener;

public interface WatchingRepository<T, ID> extends Repository<T, ID> {
    void findAndWatchById(ID id, ValueEventListener eventListener);

    void findAndWatch(ValueEventListener eventListener);
}