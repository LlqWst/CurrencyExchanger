package org.example.dao;

import DataSource.CurrenciesListener;
import org.example.entity.Currency;
import org.example.handler.CurrencyExistException;
import org.example.handler.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {

    public Currency getByCode(String code) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE Code = ?";

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException("Currency not found");
                }

                int id = rs.getInt("ID");
                String fullName = rs.getString("FullName");
                String sign = rs.getString("Sign");

                Currency currency = new Currency();
                currency.setId(id);
                currency.setCode(code);
                currency.setFullName(fullName);
                currency.setSign(sign);

                return currency;
            }
        }
    }

    public List<Currency> getAll() throws SQLException{
        String query = "SELECT * FROM Currencies";

        try (Connection connection = CurrenciesListener.getConnection()) {
            Statement statement = connection.createStatement();
            try(ResultSet rs = statement.executeQuery(query)) {
                List<Currency> currencies = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String code = rs.getString("Code");
                    String fullName = rs.getString("FullName");
                    String sign = rs.getString("Sign");

                    Currency currency = new Currency();
                    currency.setId(id);
                    currency.setCode(code);
                    currency.setFullName(fullName);
                    currency.setSign(sign);
                    currencies.add(currency);
                }

                return currencies;
            }
        }
    }

    public Currency save(Currency currency) throws SQLException {
        String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";

        if (isExist(currency.getCode())) {
            throw new CurrencyExistException("Code already exist");
        }

        String code = currency.getCode();
        String fullName = currency.getFullName();
        String sign = currency.getSign();

        try (Connection connection = CurrenciesListener.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, code);
                statement.setString(2, fullName);
                statement.setString(3, sign);
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    currency.setId(rs.getInt(1));
                }
            }
            return currency;
        }
    }

    private boolean isExist(String code) throws SQLException {
        String query = "SELECT ID FROM Currencies WHERE Code = ?";

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

}
