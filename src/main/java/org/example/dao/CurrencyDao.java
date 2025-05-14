package org.example.dao;

import org.example.entity.Currency;

import java.sql.SQLException;

public interface CurrencyDao extends DAO<Currency, Long>{
    Currency getByCode(String code) throws SQLException;

}
