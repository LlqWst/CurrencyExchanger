package dev.lqwd.dto;

import java.math.BigDecimal;

public class ExchangePairDto extends Dto {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private BigDecimal rate;
    private String pair;
    private BigDecimal amount;

    public String getBaseCurrencyCode() {
        return requireNonNull(baseCurrencyCode);
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public String getTargetCurrencyCode() {
        return requireNonNull(targetCurrencyCode);
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public BigDecimal getRate() {
        return requireNonNull(rate);
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getPair() {
        return requireNonNull(pair);
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public BigDecimal getAmount() {
        return requireNonNull(amount);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}

