package dev.lqwd.dto;

import java.math.BigDecimal;

public class ExchangePairDto extends Dto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
    private BigDecimal amount;

    public String getBaseCurrencyCode() {
        return super.requireNonNull(baseCurrencyCode);
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return super.requireNonNull(targetCurrencyCode);
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getRate() {
        return super.requireNonNull(rate);
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return super.requireNonNull(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}

