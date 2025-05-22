package dev.lqwd.dto;

import java.math.BigDecimal;
import java.util.Objects;

public record ExchangeDto (
        CurrencyDto baseCurrency,
        CurrencyDto targetCurrency,
        BigDecimal rate,
        BigDecimal amount,
        BigDecimal convertedAmount
){

    public ExchangeDto {
        Objects.requireNonNull(baseCurrency);
        Objects.requireNonNull(targetCurrency);
        Objects.requireNonNull(rate);
        Objects.requireNonNull(amount);
        Objects.requireNonNull(convertedAmount);
    }
}
