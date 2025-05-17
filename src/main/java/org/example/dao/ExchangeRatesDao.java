package org.example.dao;

import DataSource.CurrenciesListener;
import org.example.dto.ExchangeRateDto;
import org.example.entity.Currency;
import org.example.entity.ExchangeRate;
import org.example.handler.custom_exceptions.ExistInDbException;
import org.example.handler.custom_exceptions.NotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDao {

    public ExchangeRate get(ExchangeRateDto exRateDto) throws SQLException {
        String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ?";

        try (Connection connection = CurrenciesListener.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, exRateDto.getBaseCurrency().getId());
            statement.setInt(2, exRateDto.getTargetCurrency().getId());
            try (ResultSet rs = statement.executeQuery()) {

                if (!rs.next()) {
                    throw new NotFoundException();
                }

                int id = rs.getInt("ID");
                String rate = rs.getString("Rate");
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setId(id);
                exchangeRate.setRate(rate);

                return exchangeRate;
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
                    String rate = rs.getString("Rate");

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

    public ExchangeRate save(ExchangeRateDto exRateDto) throws SQLException {
        String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";

        int base = exRateDto.getBaseCurrency().getId();
        int target = exRateDto.getTargetCurrency().getId();
        String rate = exRateDto.getRate();

        if (isExist(base, target)) {
            throw new ExistInDbException();
        }

        ExchangeRate exRate = new ExchangeRate();
        exRate.setBaseCurrency(exRateDto.getBaseCurrency());
        exRate.setTargetCurrency(exRateDto.getTargetCurrency());
        exRate.setRate(rate);

        try (Connection connection = CurrenciesListener.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, base);
                statement.setInt(2, target);
                statement.setString(3, rate);
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();

                if (rs.next()) {
                    exRate.setId(rs.getInt(1));

                } else {
                    throw new RuntimeException();
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

