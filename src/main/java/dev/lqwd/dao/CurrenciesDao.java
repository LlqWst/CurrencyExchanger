package dev.lqwd.dao;

import dev.lqwd.config.CurrenciesListener;
import dev.lqwd.entity.Currency;
import dev.lqwd.exceptions.custom_exceptions.ExistInDbException;
import dev.lqwd.exceptions.custom_exceptions.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static dev.lqwd.exceptions.ErrorMessages.EXIST_CURRENCY;
import static dev.lqwd.exceptions.ErrorMessages.NOT_EXIST_CURRENCY;

public class CurrenciesDao {

    public Currency getByCode(String code) throws SQLException {
        String query = """
                    SELECT ID, FullName, Sign
                    FROM Currencies
                    WHERE Code = ?
                    """;

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, code);
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException(NOT_EXIST_CURRENCY.getMessage() + code);
                }

                int id = rs.getInt("ID");
                String name = rs.getString("FullName");
                String sign = rs.getString("Sign");

                return setCurrency(id, name, code, sign);

            }
        }
    }

    public Currency getById(int id) throws SQLException {
        String query = """
                    SELECT Code, FullName, Sign
                    FROM Currencies
                    WHERE ID = ?
                    """;

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException(NOT_EXIST_CURRENCY.getMessage());
                }

                String name = rs.getString("FullName");
                String code = rs.getString("Code");
                String sign = rs.getString("Sign");

                return setCurrency(id, name, code, sign);
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

                    Currency currency = setCurrency(id, name, code, sign);
                    currencies.add(currency);
                }

                return currencies;
            }
        }
    }

    public Currency save(Currency currency) {
        String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?)";

        String name = currency.getName();
        String code = currency.getCode();
        String sign = currency.getSign();

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
        } catch (SQLException e){
            throw new ExistInDbException(EXIST_CURRENCY.getMessage() + code);
        }
    }

    private Currency setCurrency(int id, String name, String code, String sign) {
        Currency currency = new Currency();
        currency.setId(id);
        currency.setName(name);
        currency.setCode(code);
        currency.setSign(sign);
        return currency;
    }

}
