package dev.lqwd.dto;

import java.math.BigDecimal;

public class ExchangeRateDto extends Dto {

    private int id;
    private CurrencyDto baseCurrency;
    private CurrencyDto targetCurrency;
    private BigDecimal rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyDto getBaseCurrency() {
        return requireNonNull(baseCurrency);
    }

    public void setBaseCurrency(CurrencyDto baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyDto getTargetCurrency() {
        return requireNonNull(targetCurrency);
    }

    public void setTargetCurrency(CurrencyDto targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return requireNonNull(rate);
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}

