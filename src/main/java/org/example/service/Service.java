package org.example.service;

import java.util.List;
import java.util.Optional;

public interface Service<T, K> {
    T get(K k);
    T save(T t);
    List<T> getAll(); //
}
