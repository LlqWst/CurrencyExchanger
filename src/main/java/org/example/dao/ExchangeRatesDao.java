package org.example.dao;

import org.example.config.CurrenciesListener;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {

    private final static int SCALE = 6;
    private final static BigDecimal SCALE_MULTIPLY = BigDecimal.valueOf(10).pow(SCALE);

    public ExchangeRate get(ExchangeRate exRate) throws SQLException {
        String query = "SELECT ID, Rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, exRate.getBaseCurrency().getId());
            statement.setInt(2, exRate.getTargetCurrency().getId());
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException();
                }

                int id = rs.getInt("ID");
                BigDecimal rate = rs.getBigDecimal("Rate")
                        .divide(SCALE_MULTIPLY, SCALE, RoundingMode.HALF_UP)
                        .stripTrailingZeros();
                exRate.setId(id);
                exRate.setRate(rate);

                return exRate;
            }
        }
    }

    public List<ExchangeRate> getAll() throws SQLException{
        String query = "SELECT * FROM ExchangeRates";
        try (Connection connection = CurrenciesListener.getConnection()) {
            Statement statement = connection.createStatement();
            try(ResultSet rs = statement.executeQuery(query)) {
                List<ExchangeRate> exchangeRates = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    int baseId = rs.getInt("BaseCurrencyId");
                    int targetId = rs.getInt("TargetCurrencyId");
                    BigDecimal rate = rs.getBigDecimal("Rate")
                            .divide(SCALE_MULTIPLY, SCALE, RoundingMode.HALF_UP)
                            .stripTrailingZeros();
                    ExchangeRate exRate = new ExchangeRate();
                    exRate.setId(id);
                    Currency baseCurrency = new Currency();
                    Currency targetCurrency = new Currency();
                    baseCurrency.setId(baseId);
                    targetCurrency.setId(targetId);
                    exRate.setBaseCurrency(baseCurrency);
                    exRate.setTargetCurrency(targetCurrency);
                    exRate.setRate(rate);
                    exchangeRates.add(exRate);
                }

                return exchangeRates;
            }
        }
    }

    public ExchangeRate save(ExchangeRate exRate) throws SQLException {
        String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";

        int base = exRate.getBaseCurrency().getId();
        int target = exRate.getTargetCurrency().getId();
        BigDecimal bigRate = exRate.getRate();

        int intRate = bigRate.multiply(SCALE_MULTIPLY).intValueExact();

        if (isExist(base, target)) {
            throw new ExistInDbException();
        }

        try (Connection connection = CurrenciesListener.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, base);
                statement.setInt(2, target);
                statement.setInt(3, intRate);
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) {
                    exRate.setId(rs.getInt(1));
                } else {
                    throw new SQLException();
                }
            }
            return exRate;
        }
    }

    private boolean isExist(int baseCode, int targetCode) throws SQLException {
        String query = "SELECT ID FROM ExchangeRates WHERE BaseCurrencyId = ? and TargetCurrencyId = ?";

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, baseCode);
            statement.setInt(2, targetCode);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        }
    }

}

