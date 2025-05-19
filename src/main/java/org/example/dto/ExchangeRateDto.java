package org.example.dto;

import org.example.entity.Currency;

import java.math.BigDecimal;

public record ExchangeRateDto(

     int id,
     CurrencyDto baseCurrencyDto,
     CurrencyDto targetCurrencyDto,
     BigDecimal rate
){}
//    public ExchangeRateDto() {
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public Currency getBaseCurrency() {
//        return baseCurrency;
//    }
//
//    public void setBaseCurrency(Currency baseCurrency) {
//        this.baseCurrency = baseCurrency;
//
//    }
//
//    public Currency getTargetCurrency() {
//        return targetCurrency;
//    }
//
//    public void setTargetCurrency(Currency targetCurrency) {
//        this.targetCurrency = targetCurrency;
//    }
//
//    public BigDecimal getRate() {
//        return rate;
//    }
//
//    public void setRate(BigDecimal rate) {
//        this.rate = rate;
//    }
//}
