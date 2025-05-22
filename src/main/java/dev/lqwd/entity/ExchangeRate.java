package dev.lqwd.entity;

import java.math.BigDecimal;

public class ExchangeRate extends Entity {

    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return super.requireNonNull(baseCurrency);
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return super.requireNonNull(targetCurrency);
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getRate() {
        return super.requireNonNull(rate);
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
