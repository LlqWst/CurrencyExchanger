package org.example.dto;

import java.math.BigDecimal;

public record ExchangeRateDto(

     int id,
     CurrencyDto baseCurrencyDto,
     CurrencyDto targetCurrencyDto,
     BigDecimal rate
){}
