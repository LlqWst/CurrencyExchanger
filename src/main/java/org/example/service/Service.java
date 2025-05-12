package org.example.service;

import java.util.List;

public interface Service<T, K> {
    K create(T t);
    T read(K id);
    void update(T t);
    void delete(K id);
    List<T> getAll(); //
}
