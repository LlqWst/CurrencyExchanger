package dev.lqwd.dao;

import dev.lqwd.config.CurrenciesListener;
import dev.lqwd.entity.Currency;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.exception.ExistInDataBaseException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDao {

    private final static String DB_ERROR_READ = "Failed to read currencies from the database";
    private final static String DB_ERROR_SAVE = "Failed to save currency with code '%s' to the database";
    private final static String DB_ERROR_ALREADY_EXIST = "Currency with code '%s' already exist";

    public Optional<Currency> getByCode(String code) {
        String query = """
                SELECT *
                FROM Currencies
                WHERE Code = ?
                """;

        try (Connection connection = CurrenciesListener.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, code);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(getCurrency(rs));
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_READ );
        }
    }

    public Optional<Currency> getById(Long id) {
        String query = """
                SELECT *
                FROM Currencies
                WHERE ID = ?
                """;

        try (Connection connection = CurrenciesListener.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    return Optional.empty();
                }

                return Optional.of(getCurrency(rs));
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_READ);
        }
    }

    public List<Currency> getAll() {
        String query = "SELECT * FROM Currencies";

        try (Connection connection = CurrenciesListener.getConnection();
             Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(query)) {

                List<Currency> currencies = new ArrayList<>();

                while (rs.next()) {
                    currencies.add(getCurrency(rs));
                }

                return currencies;
            }
        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_READ);
        }
    }

    public Currency save(Currency currency) {
        String sql = "INSERT INTO Currencies (Code, FullName, Sign) VALUES (?, ?, ?) RETURNING *";

        try (Connection connection = CurrenciesListener.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                throw new DataBaseException(DB_ERROR_SAVE);
            }

            return getCurrency(rs);

        } catch (SQLException e) {
            if (e instanceof SQLiteException exception) {
                if (exception.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new ExistInDataBaseException(String.format(DB_ERROR_ALREADY_EXIST, currency.getCode()));
                }
            }
            throw new ExistInDataBaseException(String.format(DB_ERROR_SAVE, currency.getCode()));
        }
    }

    private static Currency getCurrency(ResultSet rs) throws SQLException {
        return new Currency(
                rs.getLong("ID"),
                rs.getString("FullName"),
                rs.getString("Code"),
                rs.getString("Sign")
        );
    }

}
