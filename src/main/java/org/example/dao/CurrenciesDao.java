package org.example.dao;

import DataSource.CurrenciesListener;
import org.example.dto.CurrencyDto;
import org.example.entity.Currency;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CurrenciesDao {

    public Currency getByCode(CurrencyDto currencyDto) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE Code = ?";

        String code = currencyDto.getCode();

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException(code);
                }

                int id = rs.getInt("ID");
                String name = rs.getString("FullName");
                String sign = rs.getString("Sign");

                Currency currency = new Currency();
                currency.setId(id);
                currency.setName(name);
                currency.setCode(code);
                currency.setSign(sign);

                return currency;
            }
        }
    }

    public Currency getById(CurrencyDto currencyDto) throws SQLException {
        String query = "SELECT Code, FullName, Sign FROM Currencies WHERE ID = ?";

        int id = currencyDto.getId();

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException(currencyDto.getCode());
                }

                String name = rs.getString("FullName");
                String code = rs.getString("Code");
                String sign = rs.getString("Sign");

                Currency currency = new Currency();
                currency.setId(id);
                currency.setName(name);
                currency.setCode(code);
                currency.setSign(sign);

                return currency;
            }
        }
    }


    public List<Currency> getAll() throws SQLException {
        String query = "SELECT * FROM Currencies";

        try (Connection connection = CurrenciesListener.getConnection()) {
            Statement statement = connection.createStatement();
            try(ResultSet rs = statement.executeQuery(query)) {
                List<Currency> currencies = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("FullName");
                    String code = rs.getString("Code");
                    String sign = rs.getString("Sign");

                    Currency currency = new Currency();
                    currency.setId(id);
                    currency.setName(name);
                    currency.setCode(code);
                    currency.setSign(sign);
                    currencies.add(currency);
                }

                return currencies;
            }
        }
    }

    public Currency save(CurrencyDto currencyDto) throws SQLException {
        String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";

        String name = currencyDto.getName();
        String code = currencyDto.getCode();
        String sign = currencyDto.getSign();

        Currency currency = new Currency();
        currency.setName(name);
        currency.setCode(code);
        currency.setSign(sign);

        if (isExist(code)) {
            throw new ExistInDbException();
        }

        try (Connection connection = CurrenciesListener.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, code);
                statement.setString(2, name);
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
