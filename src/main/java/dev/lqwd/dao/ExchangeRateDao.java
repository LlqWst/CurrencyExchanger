package dev.lqwd.dao;

import dev.lqwd.config.CurrenciesListener;
import dev.lqwd.entity.Currency;
import dev.lqwd.entity.ExchangeRate;
import dev.lqwd.exception.DataBaseException;
import dev.lqwd.exception.ExistInDataBaseException;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDao {


    private final static String DB_ERROR_READ = "Failed to read ExchangeRates from the database";
    private final static String DB_ERROR_UPDATE = "Failed to update ExchangeRates to the database";
    private final static String DB_ERROR_SAVE = "Failed to save exchange rate '%s' to '%s' to the database";
    private final static String DB_ERROR_ALREADY_EXIST = "Exchange rate '%s' to '%s' already exists";

    private final static int SCALE = 6;
    private final static BigDecimal SCALE_MULTIPLY = BigDecimal.valueOf(10).pow(SCALE);

    public Optional<ExchangeRate> getByPair(String from, String to) {
        String query = """
                SELECT er.ID AS id,
                       b.ID AS base_id, b.Sign AS base_sign, b.FullName AS base_name, b.Code AS base_code,
                       t.ID AS target_id, t.Sign AS target_sign, t.FullName AS target_name, t.Code AS target_code,
                       er.Rate AS rate
                FROM ExchangeRates AS er
                INNER JOIN Currencies AS b ON er.BaseCurrencyId = b.ID AND b.Code = ?
                INNER JOIN Currencies AS t ON er.TargetCurrencyId = t.ID AND t.Code = ?
                """;

        try (Connection connection = CurrenciesListener.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, from);
            statement.setString(2, to);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            return Optional.of(getExchangeRate(rs));

        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_READ);
        }
    }

    public List<ExchangeRate> getAll() {

        String query = """
                SELECT er.ID AS id,
                       b.ID AS base_id, b.Sign AS base_sign, b.FullName AS base_name, b.Code AS base_code,
                       t.ID AS target_id, t.Sign AS target_sign, t.FullName AS target_name, t.Code AS target_code,
                       er.Rate AS rate
                FROM ExchangeRates AS er
                INNER JOIN Currencies AS b ON er.BaseCurrencyId = b.ID
                INNER JOIN Currencies AS t ON er.TargetCurrencyId = t.ID
                """;

        try (Connection connection = CurrenciesListener.getConnection();
             Statement statement = connection.createStatement()) {

            ResultSet rs = statement.executeQuery(query);

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (rs.next()) {
                exchangeRates.add(getExchangeRate(rs));
            }

            return exchangeRates;

        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_READ);
        }
    }

    public ExchangeRate save(ExchangeRate exRate) {
        String query = """
                INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
                VALUES (?, ?, ?)
                RETURNING ID
                """;

        long base = exRate.getBaseCurrency().getId();
        long target = exRate.getTargetCurrency().getId();
        BigDecimal rate = exRate.getRate().multiply(SCALE_MULTIPLY);

        try (Connection connection = CurrenciesListener.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, base);
            statement.setLong(2, target);
            statement.setBigDecimal(3, rate);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                throw new DataBaseException(
                        String.format(DB_ERROR_SAVE,
                                exRate.getBaseCurrency().getCode(),
                                exRate.getTargetCurrency().getCode())
                );
            }

            exRate.setId(rs.getLong("id"));
            return exRate;

        } catch (SQLException e) {
            if (e instanceof SQLiteException exception) {
                if (exception.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new ExistInDataBaseException(
                            String.format(DB_ERROR_ALREADY_EXIST,
                                    exRate.getBaseCurrency().getCode(),
                                    exRate.getTargetCurrency().getCode())
                    );
                }
            }
            throw new DataBaseException(
                    String.format(DB_ERROR_SAVE,
                            exRate.getBaseCurrency().getCode(),
                            exRate.getTargetCurrency().getCode())
            );
        }
    }

    public Optional<ExchangeRate> update(ExchangeRate exRate) {
        String query = """
                UPDATE ExchangeRates
                SET Rate = ?
                WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?
                RETURNING ID
                """;

        long base = exRate.getBaseCurrency().getId();
        long target = exRate.getTargetCurrency().getId();
        BigDecimal rate = exRate.getRate().multiply(SCALE_MULTIPLY);

        try (Connection connection = CurrenciesListener.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setBigDecimal(1, rate);
            statement.setLong(2, base);
            statement.setLong(3, target);

            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            exRate.setId(rs.getLong("ID"));
            return Optional.of(exRate);

        } catch (SQLException e) {
            throw new DataBaseException(DB_ERROR_UPDATE);
        }
    }

    private static ExchangeRate getExchangeRate(ResultSet rs) throws SQLException {
        return new ExchangeRate(
                rs.getLong("id"),
                new Currency(
                        rs.getLong("base_id"),
                        rs.getString("base_name"),
                        rs.getString("base_code"),
                        rs.getString("base_sign")
                ),
                new Currency(
                        rs.getLong("target_id"),
                        rs.getString("target_name"),
                        rs.getString("target_code"),
                        rs.getString("target_sign")
                ),
                rs.getBigDecimal("rate")
                        .movePointLeft(SCALE)
                        .stripTrailingZeros()
        );
    }

}

