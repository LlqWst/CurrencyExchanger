package org.example.dao;

import DataSource.AppContextListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.Currency;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CurrenciesDao implements DAO<Currency, String> {

    private static final String TABLE_NAME = "Currencies";

    @Override
    public Currency get(String code) throws SQLException {
        Connection conn = AppContextListener.getConnection();

        String query = "SELECT ID, FullName, Sign FROM " + TABLE_NAME + " WHERE Code = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, code);
        ResultSet rs = statement.executeQuery();

        int id = rs.getInt("ID");
        String fullName = rs.getString("FullName");
        String sign = rs.getString("Sign");
        return new Currency(id, code, fullName, sign);

    }

    @Override
    public List<Currency> getAll() throws SQLException {
        Connection conn = AppContextListener.getConnection();

        String query = "SELECT * FROM " + TABLE_NAME;
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query);

        List<Currency> currencies = new ArrayList<>();

        while (rs.next()){
            int id = rs.getInt("ID");
            String code = rs.getString("Code");
            String fullName = rs.getString("FullName");
            String sign = rs.getString("Sign");
            Currency currency = new Currency(id, code, fullName, sign);
            currencies.add(currency);
        }

        return currencies;
    }

    @Override
    public Currency save(Currency currency) throws SQLException {
        return null;
    }

    @Override
    public Currency update(Currency currency) throws SQLException {
        return null;
    }

    @Override
    public void delete(String code) throws SQLException {

    }

    private ConcurrentHashMap<Integer, Currency> currencies;
    private AtomicInteger key;

    public CurrenciesDao(){
        this.currencies = new ConcurrentHashMap<>();
        key = new AtomicInteger();

        this.addCurrency(new Currency("ipk", "sds", "&s"));
        this.addCurrency(new Currency("usr", "Doll", "$"));
        this.addCurrency(new Currency("rub", "rus Rubl", "RR"));
    }

    public  String findAllCurrencies() throws IllegalAccessException {
        List<Currency> list = new ArrayList<>(this.currencies.values());
        return toJson(list);
    }

    public boolean createCurrency(String jsonPayload){
        if(jsonPayload == null){
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            Currency currency = mapper.readValue(jsonPayload, Currency.class);
            if(currency != null){
                this.addCurrency(currency);
                return true;
            }
        } catch (Exception e) {}
        return false;

    }

    private String toJson(Object list){
        if(list == null){
            return null;
        }
        String json = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(list);
        } catch (Exception e){}
        return json;
    }

    private boolean addCurrency (Currency currency){
        int id = key.incrementAndGet();
        currency.setId(id);
        this.currencies.put(id, currency);
        return true;
    }

}
