package com.thm.hoangminh.multimediamarket.repository.base;

public interface WatchingRepository<T, ID, E> extends Repository<T, ID, E> {
    void findAndWatchById(ID id, E event);

    void findAndWatch(E event);
}