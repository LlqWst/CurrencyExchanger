package org.example.dao;

import org.example.entity.Currency;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, K> {

    Currency get(K code) throws SQLException;
    List<T> getAll() throws SQLException;
    T save(T t) throws SQLException;
    T update(T t) throws SQLException;
    void delete(K id) throws SQLException;

}
