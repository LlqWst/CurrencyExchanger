package org.example.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, K> {

    T get(K id) throws SQLException;
    List<T> getAll() throws SQLException;
    T save(T entity) throws SQLException;
    T update(T entity) throws SQLException;
    //void delete(K id) throws SQLException;

}
