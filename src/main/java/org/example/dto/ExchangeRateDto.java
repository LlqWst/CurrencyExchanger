package org.example.dto;

import java.math.BigDecimal;

public class ExchangeRateDto {

    private int id;
    private CurrencyDto baseCurrencyDto;
    private CurrencyDto targetCurrencyDto;
    private BigDecimal rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CurrencyDto getBaseCurrencyDto() {
        return baseCurrencyDto;
    }

    public void setBaseCurrencyDto(CurrencyDto baseCurrencyDto) {
        this.baseCurrencyDto = baseCurrencyDto;
    }

    public CurrencyDto getTargetCurrencyDto() {
        return targetCurrencyDto;
    }

    public void setTargetCurrencyDto(CurrencyDto targetCurrencyDto) {
        this.targetCurrencyDto = targetCurrencyDto;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}

